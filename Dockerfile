FROM amazoncorretto:17-alpine

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties .
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

EXPOSE $PORT

CMD java -Dspring.profiles.active=prod -jar build/libs/CardGeneratorVictorNicolauNeto-0.0.1-SNAPSHOT.jar
