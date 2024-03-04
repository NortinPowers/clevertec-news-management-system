#FROM gradle:8.5-jdk-alpine AS build
#
#WORKDIR /auth
#
#COPY . /auth
#
##COPY C:/Users/Нортин/.m2/repository/by/clevertec /root/.m2
#
#RUN gradle build --no-daemon
#
#FROM openjdk:17-alpine3.14
#
#COPY --from=build /news/build/libs/auth-0.0.1-SNAPSHOT.jar auth.jar
#
#CMD ["java", "-jar", "/auth.jar"]

FROM openjdk:17-jdk-slim
COPY build/libs/auth-0.0.1-SNAPSHOT.jar auth.jar
ENTRYPOINT ["java", "-jar", "auth.jar"]