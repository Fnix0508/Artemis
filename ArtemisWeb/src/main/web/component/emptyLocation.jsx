import React, {Component} from 'react';
import css from './emptyLocation.css';
import cn from 'classnames';

class Chessman extends Component {
  constructor(props) {
    super(props);
    this.state = {
      active: false
    }
  }

  active() {
    this.setState({active: true});
  }

  cleanActive() {
    this.setState({active: false});
  }

  render(){
    let {onClick} = this.props;
    let {active} = this.state;
    return (
      <div className={css.emptyBox} onClick={onClick} onMouseEnter={() => this.active()} onMouseLeave={() => this.cleanActive()}>
        <div className={cn(css.empty, active ? css.active : null)}/>
      </div>
    );
  }
}

export default Chessman;
