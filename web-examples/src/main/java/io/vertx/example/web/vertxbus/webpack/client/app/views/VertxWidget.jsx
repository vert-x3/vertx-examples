import React from 'react/addons';
import AltContainer from 'alt/AltContainer';
import classSet from 'classnames';

import VertxStore from '../stores/VertxStore.js';
import ServerInfoStore from '../stores/ServerInfoStore.js';
import VertxActions from '../actions/VertxActions.js';


class ConnectedView extends React.Component {
  render()
  {
    var statusIcon = <i className="white plug icon"></i>;
    var statusText = <span className="">{this.props.serverTime}</span>;

    return (
        <div className="ui green mini label">
          {statusIcon} {statusText}
        </div>
    );
  }
}

class DisconnectedView extends React.Component {
  render()
  {
    var inDistress = !this.props.vertx.serviceActive;
    var on = this.props.vertx.connectProgress % 2;
    var tryPercent = 100 * (this.props.vertx.connectTries / this.props.vertx.tryLimit);
    var progressBar = //null;
        (
            <div className="ui top attached progress">
              <div className="bar" style={{width: tryPercent+'%', transitionDuration: '300ms'}}></div>
            </div>
        );
    if (inDistress) {
      return (
          <a className="ui small red label status-popup">
            <i className="white minus circle icon"></i> <span>Disconnected</span>
          </a>
      )
    } else {

      return on ? (
          <div className="ui segment">
            <div className="ui small yellow label">
              {progressBar}
              <i className="yellow plug icon"></i>
              <span>Connecting</span>
            </div>

          </div>
      ) : (
          <div className="ui segment">
            <div className="ui small yellow label">
              {progressBar}
              <i className="white plug icon"></i>
              <span>Connecting</span>
            </div>
          </div>
      );
    }
  }

}

class WidgetView extends React.Component {

  render()
  {
    console.log(this.props.vertx);

    var busStat = this.props.vertx.eventBusStatus;
    var ebOpen = (busStat === 'OPEN');

    return ebOpen
        ? <ConnectedView serverTime={this.props.server.serverTime}
                         eventBusStatus={this.props.vertx.eventBusStatus}/>
        : <DisconnectedView vertx={this.props.vertx}/>;
  }
}

export default class VertxWidget extends React.Component {

  render()
  {
    return (
        <div className="ui segment">
          <AltContainer stores={{vertx: VertxStore, server: ServerInfoStore}}>
            <WidgetView />
          </AltContainer>
        </div>
    );
  }

}
