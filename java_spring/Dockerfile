FROM maven:3.6.3-jdk-8 AS build
COPY . /home/maven/src
WORKDIR /home/maven/src
RUN mvn clean install

FROM openjdk:8-jdk-alpine
EXPOSE 8006:80
RUN mkdir /app
COPY  --from=build /home/maven/src/target/java_spring-0.0.1-SNAPSHOT.jar /app/java_spring-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/app/java_spring-0.0.1-SNAPSHOT.jar"]

#docker build --tag=java_spring:latest .
