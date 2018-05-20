FROM openjdk:8-alpine
MAINTAINER x7airworker

RUN mkdir open-wrapper

COPY target/Open-Wrapper-1.0-SNAPSHOT.jar open-wrapper/

WORKDIR open-wrapper/

CMD["java", "-jar", "Open-Wrapper-1.0-SNAPSHOT.jar"]