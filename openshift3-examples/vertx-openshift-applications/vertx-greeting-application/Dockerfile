FROM fabric8/java-alpine-openjdk8-jre

EXPOSE 8080

# Copy dependencies
COPY target/dependency/* /deployment/libs/

# Copy classes
COPY target/classes /deployment/classes

ENV JAVA_APP_DIR=/deployment
ENV JAVA_LIB_DIR=/deployment/libs
ENV JAVA_CLASSPATH=${JAVA_APP_DIR}/classes:${JAVA_LIB_DIR}/*
ENV JAVA_OPTIONS="-Dvertx.cacheDirBase=/tmp"
ENV JAVA_MAIN_CLASS="io.vertx.example.openshift.greeting.MyGreetingApp"
