import React, {Component} from 'react';
import css from './iconfont.css';
import cn from 'classnames';

class Icon extends Component {
  constructor(props) {
    super(props);
  }

  render(){
    let {type = "xinxi"} = this.props;
    return (
      <i className={cn(css.iconfont, css["icon-" + type])}/>
    );
  }
}

export default Icon;
