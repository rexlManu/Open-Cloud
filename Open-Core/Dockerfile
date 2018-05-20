FROM openjdk:8-alpine
MAINTAINER x7airworker

RUN mkdir open-core

COPY target/Open-Core-1.0-SNAPSHOT.jar open-core/

WORKDIR open-core/

CMD["java", "-jar", "Open-Core-1.0-SNAPSHOT.jar"]