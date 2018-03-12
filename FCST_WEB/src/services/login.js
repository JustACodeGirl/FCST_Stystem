import { request} from '../utils'

export async function login (data) {
  return request({
    url: "/api/users/login",
    method: 'post',
    data,
  })
}
