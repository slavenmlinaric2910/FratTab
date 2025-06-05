FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy the JAR file using the exact name from your target folder
COPY --from=builder /app/target/frattab-*.jar ./app.jar
EXPOSE 12000
ENTRYPOINT ["java", "-jar", "app.jar"]
