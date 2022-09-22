FROM adoptopenjdk/openjdk11
ADD target/auth-service-0.0.1-SNAPSHOT.jar authService.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "authService.jar"]
