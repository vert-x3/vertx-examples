package io.vertx.examples.resteasy.helloworld;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/")
public class HelloWorldService {

  @GET
  @Path("/{name:.*}")
  public Response doGet(@PathParam("name") String name) {
    if (name == null || name.isEmpty()) {
      name = "World";
    }
    return Response.status(200).entity("Hello " + name).build();
  }
}