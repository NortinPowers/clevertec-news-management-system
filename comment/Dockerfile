#FROM gradle:8.5-jdk-alpine AS build
#
#WORKDIR /comment
#
#COPY . /comment
#
##COPY C:/Users/Нортин/.m2/repository/by/clevertec /root/.m2
#
#RUN gradle build --no-daemon
#
#FROM openjdk:17-alpine3.14
#
#COPY --from=build /news/build/libs/comment-0.0.1-SNAPSHOT.jar comment.jar
#
#CMD ["java", "-jar", "/comment.jar"]

FROM openjdk:17-jdk-slim
COPY build/libs/comment-0.0.1-SNAPSHOT.jar comment.jar
ENTRYPOINT ["java", "-jar", "comment.jar"]