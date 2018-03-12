import { request } from '../utils'

export async function query (params) {
  return request( {
    url:'/api/query',
    method: 'post',
    data: params,
  })
}


