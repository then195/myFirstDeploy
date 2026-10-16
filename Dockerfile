# ប្រើប្រាស់ Maven ជាមួយ OpenJDK សម្រាប់ Build Project
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ប្រើប្រាស់ OpenJDK ស្រាលសម្រាប់ Run កម្មវិធី
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# បើក Port 8080 សម្រាប់ Render
EXPOSE 8080

# រត់កម្មវិធី Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]