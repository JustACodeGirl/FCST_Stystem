import { request } from '../utils'

export async function login (params) {
  return request({
    url: "/api/users/login",
    method: 'post',
    data: params,
  })
}

export async function logout (params) {
  return request({
    url: "/api/users/logout",
    method: 'post',
    data: params,
  })
}

export async function getUserInfo (params) {
  return request({
    url: "/api/users/info",
    method: 'get',
    data: params,
  })
}
