#FROM gradle:8.5-jdk-alpine AS build
#
#WORKDIR /news
#
#COPY . /news
#
##COPY C:/Users/Нортин/.m2/repository/by/clevertec /root/.m2
#
#RUN gradle build --no-daemon
#
#FROM openjdk:17-alpine3.14
#
#COPY --from=build /news/build/libs/news-0.0.1-SNAPSHOT.jar news.jar
#
##CMD ["java", "-jar", "/news.jar"]
##TODO
#ENTRYPOINT ["java", "-jar", "news.jar"]

FROM openjdk:17-jdk-slim
COPY build/libs/news-0.0.1-SNAPSHOT.jar news.jar
ENTRYPOINT ["java", "-jar", "news.jar"]
