FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/department-service-3.5.4.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar", "app.jar"]
