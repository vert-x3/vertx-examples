package io.vertx.example.web.graphql;

import graphql.GraphQL;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.VertxDataFetcher;

import java.util.ArrayList;
import java.util.List;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
import static java.util.stream.Collectors.toList;

public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  private List<Link> links;

  @Override
  public void start() {
    prepareData();

    Router router = Router.router(vertx);
    router.route("/graphql").handler(GraphQLHandler.create(createGraphQL()));

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080);
  }

  private void prepareData() {
    User peter = new User("Peter");
    User paul = new User("Paul");
    User jack = new User("Jack");

    links = new ArrayList<>();
    links.add(new Link("https://vertx.io", "Vert.x project", peter));
    links.add(new Link("https://www.eclipse.org", "Eclipse Foundation", paul));
    links.add(new Link("http://reactivex.io", "ReactiveX libraries", jack));
    links.add(new Link("https://www.graphql-java.com", "GraphQL Java implementation", peter));
  }

  private GraphQL createGraphQL() {
    String schema = vertx.fileSystem().readFileBlocking("links.graphqls").toString();

    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

    RuntimeWiring runtimeWiring = newRuntimeWiring()
      .type("Query", builder -> {
        VertxDataFetcher<List<Link>> getAllLinks = new VertxDataFetcher<>(this::getAllLinks);
        return builder.dataFetcher("allLinks", getAllLinks);
      })
      .build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

    return GraphQL.newGraphQL(graphQLSchema)
      .build();
  }

  private void getAllLinks(DataFetchingEnvironment env, Future<List<Link>> future) {
    boolean secureOnly = env.getArgument("secureOnly");
    List<Link> result = links.stream()
      .filter(link -> !secureOnly || link.getUrl().startsWith("https://"))
      .collect(toList());
    future.complete(result);
  }
}
