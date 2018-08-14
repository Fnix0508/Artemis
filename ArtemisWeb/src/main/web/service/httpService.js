import req from 'reqwest';

export const get = (url, param) => req({url: url , method: 'get' , data: param});

export const postForm = (url, param) => req({url: url, method: 'post', data: param});

export const post = (url, param) => req({url: url, method: 'post', contentType: 'application/json', data: param ? JSON.stringify(param) : undefined});

export default {
  get,
  postForm,
  post
}

