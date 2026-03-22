# Java Shopping Cart Application

A simple Java console application that calculates shopping cart total with localization support.

## Features

- Calculate item costs (price × quantity)
- Calculate total cart cost
- Localization: English, Finnish, Swedish, Japanese
- UTF-8 encoding support
- Unit tests with JUnit 5
- JaCoCo code coverage
- Docker support

## Build

```bash
mvn clean package
```

## Run

```bash
java -jar target/shopping-cart-app-1.0-SNAPSHOT.jar
```

## Docker

```bash
docker build -t shopping-cart-app .
docker run -it shopping-cart-app
```

## Test

```bash
mvn test
```

## Project Structure

- `src/main/java/` - Java source code
- `src/main/resources/` - Localization properties files
- `src/test/java/` - Unit tests
- `Jenkinsfile` - CI/CD pipeline
- `Dockerfile` - Docker image configuration
