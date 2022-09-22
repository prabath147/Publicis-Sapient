FROM adoptopenjdk/openjdk11
ADD target/admin-service-0.0.1-SNAPSHOT.jar adminService.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "adminService.jar"]
