#FROM openjdk:17-jdk-slim
#LABEL authors="ddsha"
#WORKDIR /app
#COPY target/ birthday-app.jar  birthday-app.jar
#ENTRYPOINT ["java", "-jar", " birthday-app.jar"]

FROM openjdk:17-jdk-slim
LABEL authors="ddsha"
WORKDIR /app
COPY target/birthday-app.jar birthday-app.jar
ENTRYPOINT ["java", "-jar", "birthday-app.jar"]
