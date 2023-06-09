from openjdk:12-alpine
MAINTAINER github/ruanfonseca
COPY ./target/com.ServicePro-0.0.1-SNAPSHOT.jar /app/com.ServicePro-0.0.1-SNAPSHOT.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "com.ServicePro-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080