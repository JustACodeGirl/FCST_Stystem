import { request } from '../utils'

export async function query (params) {
  return request( {
    url:'/api/statsByQuarter',
    method: 'post',
    data: params,
  })
}
