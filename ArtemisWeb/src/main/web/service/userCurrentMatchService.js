import http from './httpService.js'

export const getCurrentMatch = () => http.get('/service/userCurrentMatch/get');

export const create = () => http.postForm('/service/userCurrentMatch/create');

export const getNextOrder = (param) => http.get('/service/userCurrentMatch/getNextOrder', param);

export const play = (param) => http.post('/service/userCurrentMatch/play', param);

export const login = (param) => http.postForm('/login/login', param);




