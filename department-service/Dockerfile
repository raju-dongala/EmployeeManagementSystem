FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/department-service-1.0.0.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-jar", "app.jar"]
