FROM openjdk:8-alpine
MAINTAINER x7airworker

RUN mkdir open-master

COPY target/Open-Master-1.0-SNAPSHOT.jar open-master/

WORKDIR open-master/

CMD["java", "-jar", "Open-Master-1.0-SNAPSHOT.jar"]