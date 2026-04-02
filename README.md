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
- JaCoCo code coverage
- Docker support
- Jenkins CI/CD pipeline

## Requirements

- Java 11 or higher
- JavaFX 17
- Maven 3.6+
- MySQL or MariaDB

## Database Setup

1. Install MySQL or MariaDB
2. Run the schema script:
   ```bash
   mysql -u root -p < database/schema.sql
   ```
3. Update database credentials in `DatabaseConnection.java` if needed

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
│   │   ├── ShoppingCartController.java # FXML controller
│   │   ├── DatabaseConnection.java    # Database connection helper
│   │   ├── LocalizationService.java   # Localization from database
│   │   └── CartService.java           # Cart persistence service
│   └── resources/
│       ├── Main.fxml                  # FXML layout
├── test/java/com/shoppingcart/
│   └── ShoppingCartAppTest.java       # Unit tests
database/
└── schema.sql                         # Database schema and data

Jenkinsfile                            # CI/CD pipeline
Dockerfile                             # Docker image
pom.xml                                # Maven config
```

## Localization

| Language | Locale Code |
|----------|-------------|
| English | en |
| Finnish | fi |
| Swedish | sv |
| Japanese | ja |
| Arabic | ar |

Localization strings are stored in the `localization_strings` table in the database.

## UI Features

- Language selector dropdown (ChoiceBox)
- Dynamic item creation based on number of items
- Per-item price and quantity input
- Real-time total calculation
- Individual item totals display
- RTL support for Arabic
- Cart records saved to database

## Database Tables

### cart_records
- `id`: Primary key
- `total_items`: Total number of items
- `total_cost`: Total cost
- `language`: Selected language
- `created_at`: Timestamp

### cart_items
- `id`: Primary key
- `cart_record_id`: Foreign key to cart_records
- `item_number`: Item number
- `price`: Item price
- `quantity`: Item quantity
- `subtotal`: Item subtotal

### localization_strings
- `id`: Primary key
- `key`: Localization key
- `value`: Localized string
- `language`: Language code

## Jenkins Pipeline

The Jenkinsfile includes:
1. Checkout
2. Build (mvn clean package)
3. Test (mvn test)
4. JaCoCo coverage report
5. Docker image build
6. Push to Docker Hub (on main branch)
