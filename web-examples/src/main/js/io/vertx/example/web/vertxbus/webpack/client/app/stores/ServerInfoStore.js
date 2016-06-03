import alt from '../libs/alt';

import BusRoutes from '../libs/vertx-bus-routes';

import VertxActions from '../actions/VertxActions';
import ServerInfoActions from '../actions/ServerInfoActions';


class ServerInfoStore {

  constructor()
  {
    var self = this;

    this.state = { serverTime: "-" };

    this.bindListeners({
      handleVertxReady: VertxActions.VERTX_READY,
      handleServerInfoMessage: ServerInfoActions.SERVER_INFO_RECEIVED
    });


  }

  handleServerInfoMessage(message)
    {    
      this.setState({serverTime: message.systemTime});
    };

  handleVertxReady(eventBus)
  {
    eventBus.registerHandler(
      BusRoutes.server_info, 
      ServerInfoActions.serverInfoReceived
      );
  }
}

export default alt.createStore(ServerInfoStore, 'ServerInfoStore');
