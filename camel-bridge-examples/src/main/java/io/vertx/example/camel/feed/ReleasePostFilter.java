package io.vertx.example.camel.feed;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import org.apache.camel.Body;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ReleasePostFilter {

  /**
   * Accepts only release announce.
   */
  public boolean isRelease(@Body SyndFeed feed) {
    SyndEntry firstEntry = (SyndEntry) feed.getEntries().get(0);
    return firstEntry.getTitle().toLowerCase().contains("release");
  }

}
