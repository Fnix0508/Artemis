import http from './httpService.js'

export const login = (username, password) => http.postForm('/service/user/login', {username, password});

export const register = (param) => http.post('/service/user/register', param);