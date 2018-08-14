import React, {Component} from 'react';
import css from './home.css';
import Chessboard from 'component/chessboard.jsx';
import Chessman from 'component/chessman.jsx';
import {getCurrentMatch, create, getNextOrder, play} from 'service/userCurrentMatchService';
import _ from 'lodash';
import Dialog from 'qnui/lib/dialog';


class Home extends Component{
  constructor(props) {
    super(props);
    this.state = {
      currentMatchId: undefined,
      roundIndex: 0,
      orderList: [],
      chessmen: [],
      turn: true
    };
  }

  componentDidMount() {
    this.init().then(() => this.getOrder());
  }

  init() {
    let self = this;
    return getCurrentMatch().then(result => {
      let p;
      if (result.currentMatch) {
        p = getNextOrder({currentMatchId: result.currentMatch.id, roundIndex: 0}).then(list => {
          list = _.sortBy(list, 'roundIndex');
          let roundIndex = list.length ? _.last(list)['roundIndex'] : 0;
          let chessmen = this.parseList(list);
          return {
            orderList: list,
            turn: roundIndex % 2 === 0,
            roundIndex,
            chessmen,
            currentMatchId: result.currentMatch.id};
        })
      } else {
        p = create().then(r => {
          return {orderList: [], roundIndex: 0, turn: true, currentMatchId: r.currentMatch.id};
        })
      }
      return p.then((state) => self.setState(state));
    })
  }

  getOrder() {
    setInterval(() => this.getNext(), 1000);
  }

  getNext() {
    let self = this;
    let {currentMatchId, roundIndex, orderList} = self.state;
    getNextOrder({currentMatchId, roundIndex}).then((list) => {
      list = _.sortBy(list, 'roundIndex');
      orderList = orderList.concat(list);
      roundIndex = orderList.length ? _.last(orderList)['roundIndex'] : 0;
      let turn;
      if (roundIndex % 2 === 0) {
        turn = true;
      }
      let chessmen = this.parseList(orderList);
      self.setState({orderList, chessmen, roundIndex, turn});
    })
  }

  playAt(x, y) {
    play({x, y}).then(result => {
      if (!result.finished) {
        return;
      }
      let msg = '';
      if (result.finishType === 'DRAW') {
        msg = '平局'
      }
      if (result.finishType === 'BLACK_WIN' || result.finishType === 'WHITE_RESIGN') {
        msg = '黑棋胜利';
      }
      if (result.finishType === 'WHITE_WIN' || result.finishType === 'BLACK_RESIGN') {
        msg = '白棋胜利';
      }

      Dialog.alert({
        content: msg,
        closable: true
      }) ;
    })
  }

  parseList(list) {
    return _.map(list, i => {return {x: i.positionX,y: i.positionY}});
  }


  render(){
    let {chessmen, turn} = this.state;
    return (
      <div className={css.main}>
        <div><span>{turn ? '该你下' : '该对手下'}</span></div>
        <Chessboard chessmen={chessmen} onClick={(x, y) => this.playAt(x, y)}/>
      </div>
    );
  }
}

export default Home;