# Use a imagem base do OpenJDK
FROM openjdk:17-alpine

ADD target/com.ServicePro-0.0.1-SNAPSHOT.jar app.jar


ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 9191
