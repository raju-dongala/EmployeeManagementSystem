FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/employee-service-1.0.0.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-jar", "app.jar"]
