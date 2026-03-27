# Java Shopping Cart Application (JavaFX)

A JavaFX GUI application that calculates shopping cart total with localization support.

## Features

- **JavaFX GUI** with FXML layout
- Calculate item costs (price × quantity)
- Calculate total cart cost
- **Localization**: English, Finnish, Swedish, Japanese, Arabic
- UTF-8 encoding support
- RTL support for Arabic
- Unit tests with JUnit 5
- JaCoCo code coverage
- Docker support
- Jenkins CI/CD pipeline

## Requirements

- Java 11 or higher
- JavaFX 17
- Maven 3.6+

## Build

```bash
mvn clean package
```

## Run

```bash
mvn javafx:run
```

Or run the JAR:
```bash
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/shopping-cart-app-1.0-SNAPSHOT.jar
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

## Test Coverage

```bash
mvn jacoco:report
```

View coverage report at: `target/site/jacoco/index.html`

## Project Structure

```
src/
├── main/
│   ├── java/com/shoppingcart/
│   │   ├── ShoppingCartApp.java       # Main application
│   │   └── ShoppingCartController.java # FXML controller
│   └── resources/
│       ├── Main.fxml                  # FXML layout
│       ├── MessagesBundle_en_US.properties
│       ├── MessagesBundle_fi_FI.properties
│       ├── MessagesBundle_sv_SE.properties
│       ├── MessagesBundle_ja_JP.properties
│       └── MessagesBundle_ar_AR.properties
└── test/java/com/shoppingcart/
    └── ShoppingCartAppTest.java       # Unit tests

Jenkinsfile                            # CI/CD pipeline
Dockerfile                             # Docker image
pom.xml                                # Maven config
```

## Localization

| Language | Locale Code |
|----------|-------------|
| English | en_US |
| Finnish | fi_FI |
| Swedish | sv_SE |
| Japanese | ja_JP |
| Arabic | ar_AR |

## UI Features

- Language selector dropdown (ChoiceBox)
- Dynamic item creation based on number of items
- Per-item price and quantity input
- Real-time total calculation
- Individual item totals display
- RTL support for Arabic

## Jenkins Pipeline

The Jenkinsfile includes:
1. Checkout
2. Build (mvn clean package)
3. Test (mvn test)
4. JaCoCo coverage report
5. Docker image build
6. Push to Docker Hub (on main branch)