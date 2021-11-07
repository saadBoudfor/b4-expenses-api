FROM openjdk:11.0.4-jre-slim
MAINTAINER sboudfor
COPY target/expenses-0.0.1-SNAPSHOT.jar expenses-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/expenses-0.0.1-SNAPSHOT.jar"]