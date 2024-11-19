package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.apiclientsdk.model.User;
import com.example.apiclientsdk.utils.SignUtils;
import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 名称API
 * 
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/name")
    public String getNamenameByGet(String name, HttpServletRequest request) {
        System.out.println(request.getHeader("yupi"));
        return "你的名字是" + name;
    }
    
    @GetMapping("/get")
    public String getNameByGet(String name, HttpServletRequest request) {
        // http://localhost:8123/api/name/?name=yupi
        return "你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) {
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");
        // TODO 实际情况应该是去数据库查是否已分配给用户，直接模拟 victory一个用户
        //   模拟接口：accessKey和secretKey需要改成从数据库调用
        //   查出来：by accessKey -> secretKey
        if (!accessKey.equals("victory")) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (Long.parseLong(nonce) > 10000) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // TODO 时间和当前时间不能超过5分钟
        // if (timestamp) {
        // }
        // TODO 实际情况中是从数据库中查出 secretKey
        String serverSign = SignUtils.genSign(body, "victorysecretkey");
        if (!sign.equals(serverSign)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // if (!accessKey.equals("victory") || !secretKey.equals("victorysecretkey")) {
        //     throw new RuntimeException("无权限");
        // }
        // TODO 调用次数➕1 
        String result = "POST 用户名字是" + user.getUsername();
        return result;
    }
}
