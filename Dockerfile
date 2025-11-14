FROM eclipse-temurin:17-jre

WORKDIR /app

COPY CardGeneratorVictorNicolauNeto/build/libs/CardGeneratorVictorNicolauNeto-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -Dspring.profiles.active=prod -jar app.jar"]