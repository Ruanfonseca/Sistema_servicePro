# Use a imagem base do OpenJDK
FROM openjdk:17-alpine

ADD target/com.ServicePro-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

RUN wget https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh -O /usr/local/bin/wait-for-it && \
    chmod +x /usr/local/bin/wait-for-it
    
CMD ["java", "-jar", "app.jar"]
