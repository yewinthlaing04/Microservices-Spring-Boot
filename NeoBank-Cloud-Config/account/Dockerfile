#start with a base image containt Java runtime
FROM openjdk:17-jdk-slim

#information who maintains the image
MAINTAINER neobank.com

#add application's jar to image
COPY target/account-0.0.1-SNAPSHOT.jar account-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java", "-jar", "account-0.0.1-SNAPSHOT.jar"]