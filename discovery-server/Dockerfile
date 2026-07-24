FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/discovery-server-3.5.4.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]