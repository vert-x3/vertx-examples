# Vertx Web Api Service Example

This example shows you a complete working application with [vertx-web-api-service](https://vertx.io/docs/vertx-web-api-service/java/) & [vertx-web-api-contract](https://vertx.io/docs/vertx-web-api-contract/java/).

You have three packages:

* `models`: Contains all models of your application. They are annotated as `@DataObject` so you can use it in other Vert.x interfaces.
* `persistence`: Contains the interfaces and implementations of persistence of your application.
* `services`: Contains the Web Api Services interfaces and implementations

The class `WebApiServiceExampleMainVerticle` contains the code that bootstrap your router using `OpenAPI3RouterFactory` and mounts your services on event bus with `ServiceBinder`.

You can find the OpenAPI spec under `resources/openapi.json`

## Run

On this directory run:

```bash
mvn clean package
java -jar target/web-api-service-example-4.1.0.CR2-fat.jar
```

Then post a transaction with `curl`:

```bash
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"id": "xdg-hjhj98", "from":"thomas@example.com","to":"francesco@example.com", "message": "items", "value": 45.67}' \
  http://localhost:8080/api/transactions


curl http://localhost:8080/api/transactions
```

Eventually check the recorded transactions:

```bash
curl http://localhost:8080/api/transactions
```
