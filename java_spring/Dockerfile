FROM openjdk:8-jdk-alpine
COPY target/java_spring-0.0.1-SNAPSHOT.jar java_spring-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/java_spring-0.0.1-SNAPSHOT.jar"]
EXPOSE 8006:80

#docker build --tag=java_spring:latest .
