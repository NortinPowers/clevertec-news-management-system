FROM openjdk:17-jdk-slim
COPY build/libs/auth-0.0.1-SNAPSHOT.jar auth.jar
ENTRYPOINT ["java", "-jar", "auth.jar"]