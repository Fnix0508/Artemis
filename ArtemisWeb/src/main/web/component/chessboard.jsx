import React, {Component} from 'react';
import css from './chessboard.css';
import _ from 'lodash';
import Chessman from './chessman.jsx';
import EmptyLocation from './emptyLocation.jsx';

const getKey = (prefix, i, j) => prefix + "_" + i + "_" + j;

class Chessboard extends Component {
  constructor(props) {
    super(props);
  }

  renderRow(i) {
    let {chessmen = [], onClick} = this.props;
    return <div key={getKey("row", i, 0)} className={css.chessmenRow}>
      {
        _.map(_.range(1, 16), j => {
          let index = _.findIndex(chessmen, {x: i, y: j});
          return index === -1 ? <EmptyLocation onClick={() => onClick(i, j)} key={getKey("empty", i, j)}/> :
            index % 2 === 0 ? <Chessman key={getKey("chessman", i, j)} type="Black"/> :
              <Chessman key={getKey("chessman", i, j)} type="White"/>
        })
      }
    </div>
  }

  render(){
    return (
      <div className={css.chessboard}>
        {
          _.map(_.range(1, 16), i => this.renderRow(i))
        }
      </div>
    );
  }
}

export default Chessboard;
