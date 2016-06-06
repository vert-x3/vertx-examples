import alt from '../libs/alt';
import VertxActions from '../actions/VertxActions';

var getEventBus = () =>
{
  const Vertx = require('vertx3-eventbus-client');
  return (new Vertx('http://localhost:8080/eventbus'));
};

class VertxStore {

  constructor()
  {
    this.eventBus = null;
    this.eventBusStatus = 'CLOSED';
    this.serviceActive = true;
    this.connectProgress = 4;
    this.connectTries = 0;
    this.connectInterval = 5;
    this.tryLimit = 8;

    this.bindListeners({
      handleVertxReady: VertxActions.VERTX_READY,
      handleVertxUnready: VertxActions.VERTX_UNREADY,
      handleVertxError: VertxActions.VERTX_ERROR,
      connectEventBus: VertxActions.DO_VERTX_CONNECT
    });
  }

  connectEventBus(selfObj)
  {
    var self = selfObj ? selfObj : this;
    var connectProgress = self.connectProgress ? self.connectProgress : 0;
    var indicator = self.connectInterval - connectProgress;
    
    connectProgress += 1;

    self.setState(
        {
          connectProgress: connectProgress
        }
    );

    if (connectProgress >= self.connectInterval) {

      const EventBus = getEventBus();

      EventBus.onopen = () =>
      {
        console.log("EVENT BUS OPEN");
        var newState = {
          eventBus: EventBus,
          connectProgress: 0,
          connectTries: 0,
          serviceActive: true
        };
        self.setState(newState);

        VertxActions.vertxReady(EventBus);
      };

      EventBus.onclose = () =>
      {
        self.setState({eventBusStatus: 'CLOSED'});
        VertxActions.vertxUnready(EventBus);
      };

      EventBus.onerror = (err) =>
      {
        console.error("EVENT BUS ERROR: " + JSON.stringify(err));
        VertxActions.vertxError(err);
      };

      var newState = {
        eventBus: EventBus,
        connectProgress: 0.0,
        connectTries: self.connectTries + 1
      };
      self.setState(newState);
    } else if ( self.connectTries < self.tryLimit ){
      window.setTimeout(
          self.connectEventBus,
          1000, self
      );
    } else {
      self.setState({
        connectProgress: 0,
        connectTries: 0,
        serviceActive: false
      })
    }
  }

  handleVertxReady(eventBus)
  {
    this.setState({eventBusStatus: 'OPEN'});
  }

  handleVertxUnready(eventBus)
  {
    this.setState({connectProgress: 0});

    window.setTimeout(
        this.connectEventBus,
        1000, this
    );
  }

  handleVertxError(err)
  {
    console.log("VERTX ERROR");
  }

  attemptConnect()
  {
    console.log("attemptConnect");
    VertxActions.doVertxConnect();
  }

}

export default alt.createStore(VertxStore, 'VertxStore');
