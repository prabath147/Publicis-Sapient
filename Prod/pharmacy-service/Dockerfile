FROM adoptopenjdk/openjdk11
ADD target/pharmacy-service-0.0.1-SNAPSHOT.jar pharmacyService.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "pharmacyService.jar"]