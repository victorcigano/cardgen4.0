FROM amazoncorretto:17-alpine

WORKDIR /app

# Copy gradle files first for better caching
COPY CardGeneratorVictorNicolauNeto/gradlew .
COPY CardGeneratorVictorNicolauNeto/gradle gradle
COPY CardGeneratorVictorNicolauNeto/build.gradle .
COPY CardGeneratorVictorNicolauNeto/settings.gradle .
COPY CardGeneratorVictorNicolauNeto/gradle.properties .

RUN chmod +x ./gradlew

# Copy source code
COPY CardGeneratorVictorNicolauNeto/src src

# Build with optimizations
RUN ./gradlew build -x test --no-daemon --parallel

EXPOSE $PORT

CMD ["java", "-Xmx512m", "-Dserver.port=$PORT", "-Dspring.profiles.active=prod", "-jar", "build/libs/CardGeneratorVictorNicolauNeto-0.0.1-SNAPSHOT.jar"]