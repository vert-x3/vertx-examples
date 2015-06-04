###
# vert.x docker example using a JavaScript verticle
# To build:
#  docker build -t sample/vertx-javascript .
# To run:
#   docker run -t -i -p 8080:8080 sample/vertx-javascript
###

# Extend vert.x image                       <1>
FROM vertx/vertx3

# Set the name of the verticle to deploy    <2>
ENV VERTICLE_NAME hello-verticle.js

# Set the location of the verticles         <3>
ENV VERTICLE_HOME /usr/verticles

EXPOSE 8080

# Copy your verticle to the container       <4>
COPY $VERTICLE_NAME $VERTICLE_HOME/

# Launch the verticle                       <5>
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/*"]
