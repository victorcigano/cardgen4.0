FROM openjdk:17-jre-slim

WORKDIR /app

COPY CardGeneratorVictorNicolauNeto/gradlew .
COPY CardGeneratorVictorNicolauNeto/gradle gradle
COPY CardGeneratorVictorNicolauNeto/build.gradle .
COPY CardGeneratorVictorNicolauNeto/settings.gradle .
COPY CardGeneratorVictorNicolauNeto/gradle.properties .
COPY CardGeneratorVictorNicolauNeto/src src

RUN chmod +x ./gradlew && ./gradlew build -x test

EXPOSE 8080

CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -Dspring.profiles.active=prod -jar build/libs/CardGeneratorVictorNicolauNeto-0.0.1-SNAPSHOT.jar"]