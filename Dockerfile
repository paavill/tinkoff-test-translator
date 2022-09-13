FROM gradle:7.4.0-jdk17-alpine as builder
WORKDIR /usr/app/
COPY . .
RUN gradle build

FROM openjdk:17.0.2-jdk
WORKDIR /usr/app
COPY --from=builder /usr/app/build/libs/$ARTIFACT_NAME .
ENTRYPOINT java -Dserver.port=$INTERNAL_PORT -Dapi-key.yandex=$API_KEY -Dspring.datasource.username=$DB_USERNAME -Dspring.datasource.password=$DB_PASSWORD -jar $ARTIFACT_NAME
