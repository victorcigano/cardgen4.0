FROM amazoncorretto:17-alpine

WORKDIR /app

COPY CardGeneratorVictorNicolauNeto/gradlew .
COPY CardGeneratorVictorNicolauNeto/gradle gradle
COPY CardGeneratorVictorNicolauNeto/build.gradle .
COPY CardGeneratorVictorNicolauNeto/settings.gradle .
COPY CardGeneratorVictorNicolauNeto/gradle.properties .
COPY CardGeneratorVictorNicolauNeto/src src

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

EXPOSE $PORT

CMD ["java", "-Dserver.port=$PORT", "-Dspring.profiles.active=prod", "-jar", "build/libs/CardGeneratorVictorNicolauNeto-0.0.1-SNAPSHOT.jar"]