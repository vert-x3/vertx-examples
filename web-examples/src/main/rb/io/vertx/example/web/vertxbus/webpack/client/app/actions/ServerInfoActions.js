import alt from '../libs/alt';

class ServerInfoActions {
  
  serverInfoReceived(err, message) {
    this.dispatch(message);
  }
}

export default alt.createActions(ServerInfoActions);
