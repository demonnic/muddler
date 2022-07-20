FROM adoptopenjdk/openjdk18:alpine-slim
COPY ./build/libs/muddle*-all.jar /app
ENTRYPOINT ["java", "-jar", "/app"]
