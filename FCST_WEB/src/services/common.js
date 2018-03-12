import { request } from '../utils'

export async function getFcstWeek () {
  return request( {
    url:'/api/getFcstWeek',
    method: 'post',
  })
}

export async function getBucketDate (params) {
  return request({
    url:'/api/getBucketDate',
    method: 'post',
    data: params,
  })
}

export async function getAccountManager () {
  return request( {
    url:'/api/getAccountManager',
    method: 'post',
  })
}

export async function getCustomer () {
  return request( {
    url:'/api/getCustomer',
    method: 'post',
  })
}
