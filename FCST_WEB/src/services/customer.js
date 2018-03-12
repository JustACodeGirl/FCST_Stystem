import { request } from '../utils'

export async function query (params) {
  console.log(params)
  return request({
    url:'/api/statsByCustomer',
    method: 'post',
    data: params,
  })
}
