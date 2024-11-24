package com.example.api_gateway;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.example.apiclientsdk.utils.SignUtils;
import com.example.apicommon.model.entity.Apiinfo;
import com.example.apicommon.model.entity.User;
import com.example.apicommon.service.InnerInterfaceInfoService;
import com.example.apicommon.service.InnerUserInterfaceInfoService;
import com.example.apicommon.service.InnerUserService;

import cn.hutool.json.JSON;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 自定义全局过滤器
 * (还是需要 自动注入的！！@Component; 不需要 @Bean自定义那么麻烦)
 */
// 忽略数据库配置注释
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1", "localhost", "0:0:0:0:0:0:0:1%0");

    private static final String INTERFACE_HOST = "http://localhost:8123";  // 这里图省事，直接固定服务器地址!!

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.拿到请求日志
        ServerHttpRequest request = exchange.getRequest();
        String url = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + url);
        log.info("请求方法：" + method);
        log.info("请求参数：" + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：" + sourceAddress);
        log.info("请求来源地址：" + request.getRemoteAddress());
        ServerHttpResponse response = exchange.getResponse();
        // 2.访问控制：黑白名单
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            // 拒绝  // Mono是一个异步操作
            return handlerNoAuth(response);
        }
        // 3.用户鉴权（ak，sk）【这里会有NPE错误】
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        // 实际情况应该是去数据库中查寻是否已分配给用户
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        if (invokeUser == null) {
            return handlerNoAuth(response);
        }
        // if (!accessKey.equals("victory")) {
        //     return handlerNoAuth(response);
        // }
        if (Long.parseLong(nonce) > 10000) {
            return handlerNoAuth(response);
        }
        long currentTime = System.currentTimeMillis() / 1000;  // s
        final long FIVE_MINUTES = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handlerNoAuth(response);
        }
        // 实际情况中从数据库中查出secretKey
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handlerNoAuth(response);
        }
        // 4.判断请求的模拟接口是否存在
        // 从数据库中查询模拟接口是否存在，以及请求方法是否匹配（还可以校验请求参数）
        Apiinfo apiinfo = null;
        try {
            apiinfo = innerInterfaceInfoService.getInterfaceInfo(url, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (apiinfo == null) {
            return handlerNoAuth(response);
        }
        // 5.请求转发，调用模拟接口
        // Mono<Void> filter = chain.filter(exchange);  // 【有问题：异步，还没执行，下面就立刻返回执行完了】
        // log.info("响应：" + response.getStatusCode());
        // return filter;

        // TODO 是否还有调用次数
        
        // 5.请求转发，调用模拟接口 + 响应日志 【解决：通过装饰器解决异步顺序问题！！】
        return handleResponseAndLog(exchange, chain, apiinfo.getId(), invokeUser.getId());
    }

    /**
     * 处理响应
     * @param exchange
     * @param chain
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    public Mono<Void> handleResponseAndLog(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应吗
            HttpStatusCode statusCode = originalResponse.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                return chain.filter(exchange);  // 降级处理返回数据
            }
            // 装饰，增强能力
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                // 等调用完转发的模拟接口后，才会执行!!
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    log.info("body instanceof Flux: {}", (body instanceof Flux));
                    if (body instanceof Flux) {  // 如果响应对象是 反应式的
                        // 从中拿到真正的body
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        // 往返回值里写数据
                        // 拼接字符串
                        return super.writeWith( fluxBody.map(dataBuffer -> {
                                // 6.调用成功，接口调用次数 + 1  invokeCount
                                try {
                                    innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                } catch (Exception e) {
                                    log.error("invokeCount error", e);
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);   // 释放内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                sb2.append("<--- {} {} \n");
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                String data = new String(content, StandardCharsets.UTF_8);  // data
                                sb2.append(data);
                                // 如果响应成功，打印日志
                                log.info(sb2.toString(), rspArgs.toArray());  
                                log.info("响应结果: " + data);
                                return bufferFactory.wrap(content);
                            })
                        );
                    } else {
                        // 8.调用失败，返回一个规范的错误码
                        log.error("<-- {} 响应code异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            // 设置response对象为装饰过的
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        } catch (Exception e) {
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);  // 降级处理返回数据
        }
    }

    Mono<Void> handlerNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    Mono<Void> handlerInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
