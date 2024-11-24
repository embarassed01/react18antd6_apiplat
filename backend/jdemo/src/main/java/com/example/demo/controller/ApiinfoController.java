package com.example.demo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.apiclientsdk.client.ApiClient;
import com.example.demo.annotation.AuthCheck;
import com.example.demo.common.BaseResponse;
import com.example.demo.common.DeleteRequest;
import com.example.demo.common.ErrorCode;
import com.example.demo.common.IdRequest;
import com.example.demo.common.ResultUtils;
import com.example.demo.constant.UserConstant;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.dto.apiinfo.ApiinfoAddRequest;
import com.example.demo.model.dto.apiinfo.ApiinfoInvokeRequest;
import com.example.demo.model.dto.apiinfo.ApiinfoQueryRequest;
import com.example.demo.model.dto.apiinfo.ApiinfoUpdateRequest;
import com.example.apicommon.model.entity.Apiinfo;
import com.example.apicommon.model.entity.User;
import com.example.apicommon.model.enums.ApiinfoStatusEnum;
import com.example.demo.service.ApiinfoService;
import com.example.demo.service.UserService;
import com.google.gson.Gson;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import static com.example.demo.constant.CommonConstant.SORT_ORDER_ASC;

import java.util.List;

/**
 * API信息接口
 */
@RestController
@RequestMapping("/apiinfo")
@Slf4j
public class ApiinfoController {

    @Resource
    private ApiinfoService apiinfoService;

    @Resource 
    private UserService userService;

    // 引入客户端模拟的实例
    @Resource 
    private ApiClient apiClient;

    // region 增删改查

    /**
     * 创建
     * @param apiinfoAddRequest
     * @param request
     * @return 新api接口id
     */
    @PostMapping("/add") 
    public BaseResponse<Long> addApiInfo(@RequestBody ApiinfoAddRequest apiinfoAddRequest, HttpServletRequest request) {
        if (apiinfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Apiinfo apiinfo = new Apiinfo();
        BeanUtils.copyProperties(apiinfoAddRequest, apiinfo);
        // 校验
        apiinfoService.validApiInfo(apiinfo, true);
        User loginUser = userService.getLoginUser(request);
        apiinfo.setUserId(loginUser.getId());
        boolean result = apiinfoService.save(apiinfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(apiinfo.getId());
    }

    /**
     * 删除
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteApiInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Apiinfo oldApiinfo = apiinfoService.getById(id);
        if (oldApiinfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可以删除
        if (!oldApiinfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean b = apiinfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     * @param apiinfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateApiInfo(@RequestBody ApiinfoUpdateRequest apiinfoUpdateRequest, HttpServletRequest request) {
        if (apiinfoUpdateRequest == null || apiinfoUpdateRequest.getId() == null || apiinfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求体为空或id为空或id非正常值");
        }
        Apiinfo apiinfo = new Apiinfo();
        BeanUtils.copyProperties(apiinfoUpdateRequest, apiinfo);
        // 参数校验
        apiinfoService.validApiInfo(apiinfo, false);
        
        User user = userService.getLoginUser(request);
        long id = apiinfoUpdateRequest.getId();
        // 判断是否存在
        Apiinfo oldApiinfo = apiinfoService.getById(id);
        if (oldApiinfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可以修改
        if (!oldApiinfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean result = apiinfoService.updateById(apiinfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据id获取
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Apiinfo> getApiInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Apiinfo apiinfo = apiinfoService.getById(id);
        return ResultUtils.success(apiinfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     * @param apiinfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/list")
    public BaseResponse<List<Apiinfo>> listApiInfo(ApiinfoQueryRequest apiinfoQueryRequest) {
        Apiinfo apiinfoQuery = new Apiinfo();
        if (apiinfoQueryRequest != null) {
            BeanUtils.copyProperties(apiinfoQueryRequest, apiinfoQuery);
        }
        QueryWrapper<Apiinfo> queryWrapper = new QueryWrapper<>(apiinfoQuery);
        List<Apiinfo> apiinfoList = apiinfoService.list(queryWrapper);
        return ResultUtils.success(apiinfoList);
    }

    /**
     * 分页获取列表
     * @param apiinfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page") 
    public BaseResponse<Page<Apiinfo>> listApiInfoByPage(ApiinfoQueryRequest apiinfoQueryRequest, HttpServletRequest request) {
        if (apiinfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Apiinfo apiinfoQuery = new Apiinfo();
        BeanUtils.copyProperties(apiinfoQueryRequest, apiinfoQuery);
        long current = apiinfoQueryRequest.getCurrent();
        long size = apiinfoQueryRequest.getPageSize();
        String sortField = apiinfoQueryRequest.getSortField();
        String sortOrder = apiinfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Apiinfo> queryWrapper = new QueryWrapper<>(apiinfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(SORT_ORDER_ASC), sortField);
        Page<Apiinfo> apiinfoPage = apiinfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(apiinfoPage);
    }

    // endregion

    /**
     * 接口发布【仅管理员可操作】
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineApiInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 1.判断该接口是否存在
        Apiinfo oldApiinfo = apiinfoService.getById(id);
        if (oldApiinfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 2.判断该接口是否可以调用
        //  直接利用 客户端SDK 模拟请求一下，能够成功就说明：可以调用该接口
        com.example.apiclientsdk.model.User user = new com.example.apiclientsdk.model.User();
        user.setUsername("test");
        String username = apiClient.getUsernameByPost(user);
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
        }
        // 3.修改接口数据库中的状态字段为1
        Apiinfo apiinfo = new Apiinfo();
        apiinfo.setId(id);
        apiinfo.setStatus(ApiinfoStatusEnum.ONLINE.getValue());
        // 更新数据库
        boolean result = apiinfoService.updateById(apiinfo);
        return ResultUtils.success(result);
    }

    /**
     * 接口下线 ⬇️[仅管理员可操作]
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineApiInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 1.判断该接口是否存在
        Apiinfo oldApiinfo = apiinfoService.getById(id);
        if (oldApiinfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 2.修改接口数据库中的状态字段为0
        Apiinfo apiinfo = new Apiinfo();
        apiinfo.setId(id);
        apiinfo.setStatus(ApiinfoStatusEnum.OFFLINE.getValue());
        // 更新数据库
        boolean result = apiinfoService.updateById(apiinfo);
        return ResultUtils.success(result);
    }

    /**
     * 测试调用
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeApiInfo(@RequestBody ApiinfoInvokeRequest apiinfoInvokeRequest, HttpServletRequest request) {
        if (apiinfoInvokeRequest == null || apiinfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = apiinfoInvokeRequest.getId();
        String userRequestParams = apiinfoInvokeRequest.getRequestParams();
        // 判断是否存在
        Apiinfo oldApiinfo = apiinfoService.getById(id);
        if (oldApiinfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (oldApiinfo.getStatus() == ApiinfoStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }
        // 
        User user = userService.getLoginUser(request);
        String accessKey = user.getAccessKey();
        String secretKey = user.getSecretKey();
        ApiClient tempClient = new ApiClient(accessKey, secretKey);
        Gson gson = new Gson();
        com.example.apiclientsdk.model.User moniUser = gson.fromJson(userRequestParams, com.example.apiclientsdk.model.User.class);
        String userNameByPost = tempClient.getUsernameByPost(moniUser);  // TODO 需要优化
        return ResultUtils.success(userNameByPost);
    }

}
