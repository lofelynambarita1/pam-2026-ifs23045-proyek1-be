FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY gradle/             gradle/
COPY gradlew             gradlew
COPY gradle.properties   gradle.properties
COPY settings.gradle.kts settings.gradle.kts
COPY build.gradle.kts    build.gradle.kts

RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

COPY src/ src/

# Pakai tasks.jar (Fat JAR) bukan shadowJar karena tidak ada shadow plugin
RUN ./gradlew jar --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN mkdir -p uploads/layanan uploads/profile

COPY --from=builder /app/build/libs/hairlogy-be.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]