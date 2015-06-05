# A simple example showing how the vertx image can be used.

FROM vertx/vertx3

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

# Set the name of the verticle to deploy
ENV VERTICLE_NAME io.vertx.example.HelloWorldVerticle

# Set vertx option
ENV VERTX_OPTIONS ""

###
# The rest of the file should be fine.
###

COPY ./verticles $VERTICLE_HOME

# We use the "sh -c" to turn around https://github.com/docker/docker/issues/5509 - variable not expanded
ENTRYPOINT ["sh", "-c"]
CMD ["vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/* $VERTX_OPTIONS"]

