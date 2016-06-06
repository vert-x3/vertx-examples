package io.vertx.example.mail;

import org.apache.commons.io.IOUtils;
import org.subethamail.smtp.helper.SimpleMessageListener;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MyMessageListener implements SimpleMessageListener {


  @Override
  public boolean accept(String from, String recipient) {
    return true;
  }

  @Override
  public void deliver(String from, String recipient, InputStream data) throws IOException {
    System.out.println("Sending mail from " + from + " to " + recipient
        + " (size: " + IOUtils.toByteArray(data).length + " bytes)");
  }
}
