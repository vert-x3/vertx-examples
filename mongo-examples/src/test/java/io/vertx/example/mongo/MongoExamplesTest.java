package io.vertx.example.mongo;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoService;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MongoExamplesTest {
  MongoExamples mongoExamples;
  MongoService mongoServiceMock;

  @Before
  public void setUp() {
    this.mongoServiceMock = mock(MongoService.class);
    this.mongoExamples = new MongoExamples(this.mongoServiceMock);
  }

  @Test
  public void testSave() {
    this.mongoExamples.save(System.out::println);
    verify(this.mongoServiceMock).save(eq("books"), eq(new JsonObject().put("title","book title")), Mockito.anyObject());
  }

}
