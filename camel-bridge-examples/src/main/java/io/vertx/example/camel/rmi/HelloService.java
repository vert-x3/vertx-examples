package io.vertx.example.camel.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public interface HelloService extends Remote {

  String hello(String name) throws RemoteException;


}
