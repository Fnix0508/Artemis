import { Router, Route } from 'react-router';
import React from 'react'
import {render} from 'react-dom';
import Home from 'pages/home/home.jsx';
import Login from 'pages/login/login.jsx';
import {getHistory} from "service/commonUtils";

render(
  <Router history={getHistory()}>
    <Route path="/home" component={Home}/>
    <Route path="/login" component={Login}/>
  </Router>
  , document.getElementById('root'));