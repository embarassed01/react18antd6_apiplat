package com.example.demo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.annotation.AuthCheck;
import com.example.demo.common.BaseResponse;
import com.example.demo.common.DeleteRequest;
import com.example.demo.common.ErrorCode;
import com.example.demo.common.ResultUtils;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.dto.apiinfo.ApiinfoAddRequest;
import com.example.demo.model.dto.apiinfo.ApiinfoQueryRequest;
import com.example.demo.model.dto.apiinfo.ApiinfoUpdateRequest;
import com.example.demo.model.entity.Apiinfo;
import com.example.demo.model.entity.User;
import com.example.demo.service.ApiinfoService;
import com.example.demo.service.UserService;

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
    @AuthCheck(mustRole = "admin")
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

}
