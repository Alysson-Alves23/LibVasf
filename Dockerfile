# Etapa 1: Build
FROM maven:3.8-openjdk-17 as build
WORKDIR /src/
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Etapa 2: Run
FROM openjdk:17-slim as runtime
WORKDIR /src/
COPY --from=build /src/target/Libvasf-1.0-SNAPSHOT-shaded.jar /src/Libvasf/app.jar

# Instalar os módulos JavaFX no contêiner
RUN apt-get update && apt-get install -y openjfx

CMD ["java", "--module-path", "/usr/share/openjfx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "/src/Libvasf/app.jar"]
