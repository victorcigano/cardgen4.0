FROM openjdk:17-jdk-slim

WORKDIR /app

COPY CardGeneratorVictorNicolauNeto/gradlew .
COPY CardGeneratorVictorNicolauNeto/gradle gradle
COPY CardGeneratorVictorNicolauNeto/build.gradle .
COPY CardGeneratorVictorNicolauNeto/settings.gradle .
COPY CardGeneratorVictorNicolauNeto/gradle.properties .
COPY CardGeneratorVictorNicolauNeto/src src

RUN chmod +x ./gradlew
RUN ./gradlew build -x test

EXPOSE 8080

CMD ["java", "-jar", "build/libs/CardGeneratorVictorNicolauNeto-0.0.1-SNAPSHOT.jar"]