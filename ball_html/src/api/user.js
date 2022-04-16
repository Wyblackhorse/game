import request from '@/utils/request'

export function login(data) {
  if (data.user === 'admin') {
    data.isAdmin = 200
    return request({
      url: 'auth/login',
      method: 'post',
      data
    })
  } else {
    data.isAdmin = 100
    return request({
      url: 'auth/login',
      method: 'post',
      data
    })
  }
}

export function getInfo(token) {
  return request({
    url: '/auth/userinfo',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: 'auth/logout',
    method: 'post'
  })
}

