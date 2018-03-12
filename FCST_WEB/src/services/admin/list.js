import { request } from '../../utils'

export async function query (params) {
  return request('/api/admin/users', {
    method: 'get',
    data: params,
  })
}

export async function create (params) {
  return request('/api/admin/users', {
    method: 'post',
    data: params,
  })
}

export async function remove (params) {
  return request('/api/admin/users', {
    method: 'delete',
    data: params,
  })
}

export async function update (params) {
  return request('/api/admin/users', {
    method: 'put',
    data: params,
  })
}
