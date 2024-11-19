package com.example.apiclientsdk.client;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.example.apiclientsdk.model.User;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import com.example.apiclientsdk.utils.SignUtils;

/**
 * 调用第三方接口的客户端
 */
public class ApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8088";  // http://localhost:8123

    private String accessKey;

    private String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.secretKey = secretKey;
        this.accessKey = accessKey;
    }

    public String getNameByGet(String name) {
        // 可以单独传入http参数，这样参数会自动做URL编码
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        // hashMap.put("secretKey", secretKey);  // 一定不能直接发送！！
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.genSign(body, secretKey));
        return hashMap;
    }

    public String getUsernameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
            .addHeaders(getHeaderMap(json))
            .body(json)
            .execute();
        System.out.println(httpResponse);
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }
}
