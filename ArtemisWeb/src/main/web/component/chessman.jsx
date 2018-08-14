import React, {Component} from 'react';
import css from './chessman.css';

class Chessman extends Component {
  constructor(props) {
    super(props);
  }

  render(){
    let {type} = this.props;
    return (
      <div className={css['chessman' + type]}/>
    );
  }
}

export default Chessman;
