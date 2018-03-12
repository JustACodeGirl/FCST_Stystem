import { request } from '../../utils'

export async function query (params) {
  return request('/api/admin/role', {
    method: 'get',
    data: params,
  })
}

export async function create (params) {
  return request('/api/admin/role', {
    method: 'post',
    data: params,
  })
}

export async function remove (params) {
  return request('/api/admin/role', {
    method: 'delete',
    data: params,
  })
}

export async function update (params) {
  return request('/api/admin/role', {
    method: 'put',
    data: params,
  })
}

export async function config (params) {
  return request('/api/admin/role/config', {
    method: 'post',
    data: params,
  })
}
