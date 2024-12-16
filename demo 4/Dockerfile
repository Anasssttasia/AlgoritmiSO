# Folosim imaginea oficială de Maven pentru a construi aplicația
FROM maven:3.8-openjdk-17 AS build


# Setăm directorul de lucru
WORKDIR /app

# Copiem fișierele pom.xml și sursele în container
COPY pom.xml .
COPY src ./src

# Construim aplicația cu Maven
RUN mvn clean package -DskipTests

# Folosim o imagine de runtime de Java pentru rulare
FROM openjdk:17-jdk-slim

# Setăm directorul de lucru în container
WORKDIR /app

# Copiem fișierul JAR generat de Maven în imaginea de runtime
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expunem portul pe care va asculta aplicația
EXPOSE 8080

# Comanda pentru a rula aplicația
ENTRYPOINT ["java", "-jar", "app.jar"]
