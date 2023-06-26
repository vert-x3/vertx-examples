// the location for your service proxy is dependent on the generated code
// path, usually src/main/generated, for demo purposes the file has been
// copied to the current directory
import {MyService} from './my_service-proxy'

declare const EventBus : any;

const eb = new EventBus("http://localhost:8080/eventbus");

eb.onopen = () => {
  const myService = new MyService(eb, 'proxy.example');

  myService.sayHello('Paulo', (err, res) => {
    if (err) {
      alert('Error: ' + err);
    } else {
      alert(res);
    }
  });
};
