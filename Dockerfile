FROM openjdk:17-jdk-slim
LABEL org.opencontainers.image.authors="Deendayal Kumawat <codewithcup.developer@gmail.com>"
LABEL org.opencontainers.image.title="Birthday Notification Service"
LABEL org.opencontainers.image.version="1.0.0"
LABEL org.opencontainers.image.description="Spring Boot application for sending birthday, anniversary, and festival notifications"
LABEL org.opencontainers.image.created="2025-04-13T12:00:00Z"
LABEL org.opencontainers.image.source="https://github.com/ddsha441981/Birthday-Notification"
LABEL org.opencontainers.image.url="https://birthday-app.company.com"
LABEL org.opencontainers.image.documentation="https://github.com/ddsha441981/Birthday-Notification/blob/master/README.md"
LABEL org.opencontainers.image.license="MIT"
LABEL org.opencontainers.image.revision="26d8ad6"
LABEL com.company.team="Notification Team"
LABEL com.company.contact="codewithcup.developer@gmail.com"
WORKDIR /app
COPY target/birthday-app.jar birthday-app.jar
#Actuator
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "birthday-app.jar"]
