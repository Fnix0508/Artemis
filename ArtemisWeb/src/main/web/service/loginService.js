import http from './httpService.js'

export const login = (username, password) => http.postForm('/login/login', {username, password});

export const register = (param) => http.post('/login/register', param);