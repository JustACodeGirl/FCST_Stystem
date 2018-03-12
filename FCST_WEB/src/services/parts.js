import { request } from '../utils'

export async function query (params) {
  return request({
    url:'/api/statsByPart',
    method: 'post',
    data: params,
  })
}
