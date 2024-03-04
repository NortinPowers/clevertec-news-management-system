#FROM gradle:8.5-jdk-alpine AS build
#
#WORKDIR /gateway
#
#COPY . /gateway
#
##COPY C:/Users/Нортин/.m2/repository/by/clevertec /root/.m2
#
#RUN gradle build --no-daemon
#
#FROM openjdk:17-alpine3.14
#
#COPY --from=build /gateway/build/libs/gateway-0.0.1-SNAPSHOT.jar gateway.jar
#
#CMD ["java", "-jar", "/gateway.jar"]

FROM openjdk:17-jdk-slim
COPY build/libs/gateway-0.0.1-SNAPSHOT.jar gateway.jar
ENTRYPOINT ["java", "-jar", "gateway.jar"]
