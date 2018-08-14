import React, {Component} from 'react';
import css from './greyLink.css';
import {goTo} from "service/commonUtils"

class GreyLink extends Component {
  constructor(props) {
    super(props);
  }

  render(){
    let {href} = this.props;
    return (
      <span onClick={() => goTo(href)} className={css.main}>{this.props.children}</span>
    );
  }
}

export default GreyLink;
