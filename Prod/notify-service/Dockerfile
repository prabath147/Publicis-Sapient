FROM adoptopenjdk/openjdk11
ADD target/notify-server-0.0.1-SNAPSHOT.jar notifyService.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "notifyService.jar"]
