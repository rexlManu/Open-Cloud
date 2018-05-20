FROM openjdk:8-alpine
MAINTAINER x7airworker

RUN mkdir open-network

COPY target/Open-Network-1.0-SNAPSHOT.jar open-network/

WORKDIR open-network/

CMD["java", "-jar", "Open-Network-1.0-SNAPSHOT.jar"]