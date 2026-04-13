# Java Shopping Cart Application (JavaFX)

A JavaFX GUI application that calculates shopping cart total with localization support and database integration.

## Features

- **JavaFX GUI** with FXML layout
- Calculate item costs (price × quantity)
- Calculate total cart cost
- **Database Integration**: MySQL/MariaDB for localization and cart records
- **Localization**: English, Finnish, Swedish, Japanese, Arabic (stored in database)
- UTF-8 encoding support
- RTL support for Arabic
- Unit tests with JUnit 5
- JaCoCo code coverage (target ≥80%)
- SonarQube integration for quality analysis
- Docker support
- Jenkins CI/CD pipeline

## Requirements

- Java 11 or higher
- JavaFX 17
- Maven 3.6+
- MySQL or MariaDB (for runtime)

## Database Setup

1. Install MySQL or MariaDB
2. Run the schema script:
   ```bash
   mysql -u root -p < database/schema.sql
   ```
3. Create a `config.properties` file in the project root (or classpath) with your DB credentials:
   ```properties
   db.url=jdbc:mysql://localhost:3306/shopping_cart_localization?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.username=root
   db.password=your_password
   ```
   **Do not commit this file**; it's listed in `.gitignore`. Use `config.properties.example` as a template.

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

## Test

Run unit tests and generate coverage report:

```bash
mvn clean test jacoco:report
```

View coverage at: `target/site/jacoco/index.html`

## SonarQube Analysis

### Prerequisites

- SonarQube server running locally (default: http://localhost:9000) or SonarCloud account.
- Use a SonarQube user token for analysis authentication.

### Run Analysis

```bash
mvn sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
   -Dsonar.token=<your_sonar_token>
```

Or for SonarCloud, use your token.

### Quality Gate

The project is configured to meet:

- **Coverage ≥ 80%** (achieved by comprehensive unit tests for `CartService` and `LocalizationService`; UI classes excluded from coverage)
- **No security vulnerabilities** (A rating)
- **No bugs**

### Exclusions

To focus on business logic, coverage excludes:

- `ShoppingCartController.java` (JavaFX UI)
- `ShoppingCartApp.java` (JavaFX launcher)
- `DatabaseConnection.java` (infrastructure)

## Code Quality Standards

- **Unit tests** cover service layer using Mockito for database mocking.
- **External configuration** for database credentials (no hardcoded secrets).
- **Try-with-resources** for all JDBC resources to prevent leaks.
- **Input validation** in controller (positive numbers, field checks).

## Project Structure

```
src/
├── main/
│   ├── java/com/shoppingcart/
│   │   ├── ShoppingCartApp.java          # Main application (JavaFX)
│   │   ├── ShoppingCartController.java   # FXML controller (UI)
│   │   ├── DatabaseConnection.java       # DB connection (config-based)
│   │   ├── LocalizationService.java      # Localization from DB
│   │   └── CartService.java              # Cart persistence service
│   └── resources/
│       └── Main.fxml                      # FXML layout
├── test/java/com/shoppingcart/
│   ├── ShoppingCartAppTest.java           # Utility method tests
│   ├── CartServiceTest.java              # Service layer tests (mocked DB)
│   └── LocalizationServiceTest.java      # Service layer tests (mocked DB)
database/
└── schema.sql                             # Database schema and data
```

## CI/CD

Jenkins pipeline includes:

1. Checkout
2. Build (`mvn clean package`)
3. Test (`mvn test`)
4. JaCoCo coverage report
5. Docker image build
6. Push to Docker Hub (on main branch)
7. SonarQube analysis (quality gate)

## Security Notes

- Database credentials are loaded from `config.properties` (not committed).
- SQL injection prevented via `PreparedStatement`.
- Resource cleanup via try-with-resources.
- No sensitive information logged.

## License

This project is for educational purposes.
