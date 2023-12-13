#FROM openjdk:11
#LABEL maintainer="smartisoft"
#ARG JAR_FILE=build/libs/demo-1.0.jar
#ADD ${JAR_FILE} docker-springboot01.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-springboot01.jar"]
FROM openjdk:11
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]