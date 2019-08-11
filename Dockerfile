FROM adoptopenjdk/openjdk8:alpine-slim
COPY ./build/libs/muddle*-all.jar /app
ENTRYPOINT sh -c 'java -jar /app'
