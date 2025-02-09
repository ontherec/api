FROM openjdk:17-alpine
COPY build/libs/*.jar application.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "application.jar"]
