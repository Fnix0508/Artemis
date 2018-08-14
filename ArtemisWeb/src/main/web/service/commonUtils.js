import _ from 'lodash';
import createHashHistory from 'history/lib/createHashHistory'
import {useRouterHistory} from 'react-router'

let history = undefined;

export const getQuery = (key) => {
  let search = window.location.search;
  if (!search || search.length === 0) {
    return undefined;
  }
  let param = {};
  let list = search.split('&');
  _.each(list, i => {
    let arr = i.split('=');
    _.set(param, arr[0], arr[1]);
  })
  if (key) {
    return _.get(param, key);
  }
  return param;
}

export const getHistory = () => history ? history : useRouterHistory(createHashHistory)();

export const goTo = (url, search) => {
  getHistory().push(url, search);
}

export const goBack = () => getHistory().goBack();