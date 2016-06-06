package io.vertx.example.mail;

import org.subethamail.smtp.auth.LoginFailedException;
import org.subethamail.smtp.auth.PlainAuthenticationHandlerFactory;
import org.subethamail.smtp.auth.UsernamePasswordValidator;
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;
import org.subethamail.smtp.server.SMTPServer;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class LocalSmtpServer {

  public static void start(int port) {
    SMTPServer smtpServer = new SMTPServer(new SimpleMessageListenerAdapter(new MyMessageListener()));
    smtpServer.setPort(port);
    smtpServer.start();
  }


  public static void startWithAuth(int port) {
    SMTPServer server = new SMTPServer(new SimpleMessageListenerAdapter(new MyMessageListener()));
    server.setPort(port);
    UsernamePasswordValidator validator = (s, s1) -> {
      if (!"username".equalsIgnoreCase(s) || !"password".equalsIgnoreCase(s1)) {
        throw new LoginFailedException();
      }
    };
    server.setAuthenticationHandlerFactory(new PlainAuthenticationHandlerFactory(validator));
    server.start();
  }

}
