package com.example.api_gateway;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.example.apiclientsdk.utils.SignUtils;

import cn.hutool.json.JSON;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 自定义全局过滤器
 * (还是需要 自动注入的！！@Component; 不需要 @Bean自定义那么麻烦)
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.拿到请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + request.getPath().value());
        log.info("请求方法：" + request.getMethod());
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
        if (!accessKey.equals("victory")) {
            return handlerNoAuth(response);
        }
        if (Long.parseLong(nonce) > 10000) {
            return handlerNoAuth(response);
        }
        long currentTime = System.currentTimeMillis() / 1000;  // s
        final long FIVE_MINUTES = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handlerNoAuth(response);
        }
        String serverSign = SignUtils.genSign(body, "victorysecretkey");
        if (!sign.equals(serverSign)) {
            return handlerNoAuth(response);
        }
        // 4.判断请求的模拟接口是否存在
        // TODO 从数据库中查询模拟接口是否存在，以及请求方法是否匹配（还可以校验请求参数）
        // 5.请求转发，调用模拟接口
        // Mono<Void> filter = chain.filter(exchange);  // 【有问题：异步，还没执行，下面就立刻返回执行完了】
        // log.info("响应：" + response.getStatusCode());

        // 6.响应日志 【解决：通过装饰器解决异步顺序问题！！】
        return handleResponseLog(exchange, chain);

        // return filter;
    }

    /**
     * 处理响应
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponseLog(ServerWebExchange exchange, GatewayFilterChain chain) {
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
                                // 7.TODO 调用成功，接口调用次数 + 1  invokeCount
                                
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
