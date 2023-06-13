# Primeiro estágio: Constrói a imagem do aplicativo Java
FROM openjdk:12-alpine AS builder
MAINTAINER github/ruanfonseca
COPY ./target/com.ServicePro-0.0.1-SNAPSHOT.jar /app/com.ServicePro-0.0.1-SNAPSHOT.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "com.ServicePro-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080

# Segundo estágio: Imagem final com PostgreSQL
FROM postgres:latest



# Opcional: Defina variáveis de ambiente para o PostgreSQL
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD ruan123
ENV POSTGRES_DB ServiceProDB

# Opcional: Copie scripts SQL para o diretório de inicialização do PostgreSQL
COPY init.sql /docker-entrypoint-initdb.d/

# Opcional: Expõe a porta padrão do PostgreSQL (5432)
EXPOSE 5432
