ARG profile=default

FROM maven:3.8.4-openjdk-11-slim AS build
RUN mkdir /source && mkdir /app
WORKDIR /source
COPY pom.xml /source/pom.xml
COPY src /source/src
RUN mvn clean install
RUN cp target/b4-expenses.jar /app/b4-expenses.jar


FROM openjdk:11-jre-slim AS deploy
RUN apt update -y && apt install curl -y
ARG profile
ENV PROFILE $profile
RUN adduser user 
RUN mkdir app && mkdir -p /data
RUN chown -R user /data
VOLUME /data
WORKDIR /app
COPY --from=build /app/b4-expenses.jar .
EXPOSE 8084
USER user
CMD ["java", "-jar", "b4-expenses.jar", "--spring.profiles.active=${PROFILE}", "--server.port=8084"]
