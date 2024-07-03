FROM bellsoft/liberica-openjdk-alpine:21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon

FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
