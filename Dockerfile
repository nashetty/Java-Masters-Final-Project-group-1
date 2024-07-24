FROM eclipse-temurin:22-jdk
WORKDIR /src
COPY target/green-energy-manager-0.0.1-SNAPSHOT.jar /src/green-energy-manager.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "green-energy-manager.jar"]