// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 此处后端没有提供注释 POST /apiinfo/add */
export async function addApiInfo(body: API.ApiinfoAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/apiinfo/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /apiinfo/delete */
export async function deleteApiInfo(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/apiinfo/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /apiinfo/get */
export async function getApiInfoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getApiInfoByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseApiinfo>('/apiinfo/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /apiinfo/invoke */
export async function invokeApiInfo(
  body: API.ApiinfoInvokeRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseObject>('/apiinfo/invoke', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /apiinfo/list */
export async function listApiInfo(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listApiInfoParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListApiinfo>('/apiinfo/list', {
    method: 'GET',
    params: {
      ...params,
      apiinfoQueryRequest: undefined,
      ...params['apiinfoQueryRequest'],
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /apiinfo/list/page */
export async function listApiInfoByPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listApiInfoByPageParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageApiinfo>('/apiinfo/list/page', {
    method: 'GET',
    params: {
      ...params,
      apiinfoQueryRequest: undefined,
      ...params['apiinfoQueryRequest'],
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /apiinfo/offline */
export async function offlineApiInfo(body: API.IdRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/apiinfo/offline', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /apiinfo/online */
export async function onlineApiInfo(body: API.IdRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/apiinfo/online', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /apiinfo/update */
export async function updateApiInfo(
  body: API.ApiinfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/apiinfo/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
