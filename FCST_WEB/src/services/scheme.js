import { request } from '../utils'

export async function query (params) {
  return request( {
    url:'/api/schemes',
    method: 'get',
    data: params,
  })
}
