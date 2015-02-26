package io.vertx.example.unit;


import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@RunWith(AllTests.class)
public class JUnitTestSuite {

  public static TestSuite suite() {
    return new Tests().suite().toJUnitSuite();
  }

}
