import React, {Component} from 'react';
import css from './login.css';
import Icon from 'component/icon.jsx';
import Button from 'qnui/lib/button';
import Input from 'qnui/lib/input'
import {getQuery, goBack, goTo} from 'service/commonUtils';
import {login, register} from 'service/loginService';
import _ from 'lodash';

const type = getQuery('type');

class Login extends Component{
  constructor(props) {
    super(props);
    this.state = {}
  }

  componentDidMount() {
    this.setState({type: type});
  }

  switchRegister() {
    this.setState({type: 'register'})
  }

  toLogin() {
    let {username, password} = this.state;
    login(username, password).then(() => goTo('/home'));
  }

  toRegister() {
    let {username, password, nick} = this.state;
    register({username, password, nick}).then(() => login(username, password).then(goBack));
  }

  changeParam(key, val) {
    let newState = _.set({}, key, val);
    this.setState(newState);
  }

  render(){
    let {type = 'login'} = this.state;
    return (
      <div className={css.main}>
        <div className={css.row}><Icon type="geren"/><Input className={css.inputMargin} onChange={(val) => this.changeParam('username', val)} placeholder="用户名" /></div>
        <div className={css.row}><Icon type="yanzhengma"/><Input className={css.inputMargin} onChange={(val) => this.changeParam('password', val)} htmlType="password" placeholder="用户名"/></div>
        {type !== 'register' ? null : <div className={css.row}><Icon type="xiangqu"/><Input className={css.inputMargin} onChange={(val) => this.changeParam('nick', val)} placeholder="昵称"/></div>}
        {
          type === 'register' ? null : <div className={css.row}>
            <Button onClick={() => this.toLogin()} type="primary">登录</Button>
            <span onClick={() => this.switchRegister()} className={css.register}>去注册</span>
          </div>
        }
        {
          type !== 'register' ? null : <div className={css.row}><Button onClick={() => this.toRegister()} type="primary">注册</Button></div>
        }
      </div>
    );
  }
}

export default Login;