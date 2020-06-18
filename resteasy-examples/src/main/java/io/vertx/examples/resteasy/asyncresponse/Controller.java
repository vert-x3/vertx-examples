package io.vertx.examples.resteasy.asyncresponse;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class Controller {

  @GET
  @Path("/products/{productID}")
  @Produces({MediaType.APPLICATION_JSON})
  public void get(

      // Suspend the request
      @Suspended final AsyncResponse asyncResponse,

      // Inject the Vertx instance
      @Context Vertx vertx,

      @PathParam("productID") String productID
      ) {

    if (productID == null) {
      asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
      return;
    }

    // Send a get message to the backend
    vertx.eventBus().<JsonObject>request("backend", new JsonObject()
      .put("op", "get")
      .put("id", productID), msg -> {

      // When we get the response we resume the Jax-RS async response
      if (msg.succeeded()) {
        JsonObject json = msg.result().body();
        if (json != null) {
          asyncResponse.resume(json.encode());
        } else {
          asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
        }
      } else {
        asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
      }
    });
  }

  @PUT
  @Path("/products/{productID}")
  @Produces({MediaType.APPLICATION_JSON})
  public void put(

      // Suspend the request
      @Suspended final AsyncResponse asyncResponse,

      // Inject the Vertx instance
      @Context Vertx vertx,

      @PathParam("productID") String productID,  String product
  ) {

    if (productID == null || product == null) {
      asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
      return;
    }

    JsonObject productJson;
    try {
      productJson = new JsonObject(product);
    } catch (Exception e) {
      asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
      return;
    }

    // Send an add message to the backend
    vertx.eventBus().<Boolean>request("backend", new JsonObject()
      .put("op", "add")
      .put("id", productID)
      .put("product", productJson), msg -> {

      // When we get the response we resume the Jax-RS async response
      if (msg.succeeded()) {
        if (msg.result().body()) {
          asyncResponse.resume(Response.status(Response.Status.OK).build());
        } else {
          asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
        }
      } else {
        asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
      }
    });
  }

  @GET
  @Path("/products")
  @Produces({MediaType.APPLICATION_JSON})
  public void list(

      // Suspend the request
      @Suspended final AsyncResponse asyncResponse,

      // Inject the Vertx instance
      @Context Vertx vertx) {

    // Send a list message to the backend
    vertx.eventBus().<JsonArray>request("backend", new JsonObject().put("op", "list"), msg -> {

      // When we get the response we resume the Jax-RS async response
      if (msg.succeeded()) {
        JsonArray json = msg.result().body();
        if (json != null) {
          asyncResponse.resume(json.encode());
        } else {
          asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
        }
      } else {
        asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
      }
    });
  }
}
