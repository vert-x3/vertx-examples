package hello;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class IndexTest {

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();

    private Vertx vertx;

    @Before
    public void setUp(TestContext test) {
        vertx = rule.vertx();
        vertx.deployVerticle(MainVerticle.class.getName(), test.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext test) {
        vertx.close(test.asyncAssertSuccess());
    }

    @Test(timeout = 1000l)
    public void testHello(TestContext test) {
        Async async = test.async();

        vertx.createHttpClient()
                .getNow(MainVerticle.PORT, "localhost", "/", response -> {
                    test.assertEquals(response.statusCode(), 200);
                    response.bodyHandler(body -> {
                        test.assertTrue(body.length() > 0);
                        async.complete();
                    });
                });
    }

}
