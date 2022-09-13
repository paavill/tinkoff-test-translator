# tinkoff-test-translator
#### Text translator word by word.
## API
Available one route: **/word-by-word-translator/translate** . Documentation available at /swagger-ui.html

**input application/json:**
```
{
  "originalLanguage": "string",
  "targetLanguage": "string",
  "translatedString": "string"
}
```

**output appliaction/json:**
```
{
  "translatedString": "string"
}
```
## DB
Used H2 with in memory mode. To check the console is available at: host:port/h2-console. The username and password are set in application.properties or when the Docker container is started.
## Dockerfile / Docker-compose
You can configure startup options via the .env file.

In this file, as well as in application.properties, there is an API-KEY for accessing the Yandex translation service (it was left on purpose - to simplify the launch).

To run a container using docker-compose, just write:
```sh
docker-compose up
```