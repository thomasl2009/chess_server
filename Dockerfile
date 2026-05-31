FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/game-server-1.0.jar ./app.jar
#i changed the name of my jar file above
CMD ["java", "-jar", "app.jar"]
#im not using springboot so I didn't need to expose the port and instead could directly run the jar file