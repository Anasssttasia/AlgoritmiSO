# Folosește o imagine de bază pentru Java
FROM openjdk:23-jdk-slim

# Setează directorul de lucru în container
WORKDIR /app

# Copiază fișierul JAR construit din target în container
COPY target/demo-0.0.1-SNAPSHOT.jar demo.jar

# Expune portul pe care aplicația va rula
EXPOSE 8080

# Comanda pentru a porni aplicația
ENTRYPOINT ["java", "-jar", "demo.jar"]
