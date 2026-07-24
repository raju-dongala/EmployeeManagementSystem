FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/api-gateway-3.5.4.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]