
FROM openjdk:8-jre-alpine

ENV JAVA_APP_JAR simple-web-application-3.5.1-fat.jar

EXPOSE 8080

ADD target/$JAVA_APP_JAR /app/
RUN chmod 777 /app/

WORKDIR /app/
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $JAVA_APP_JAR"]
