# Science Fiction Book Collection API

A Spring Boot REST API for managing a science fiction book collection. This API provides CRUD operations for books with support for authors and collections.

## Features

- RESTful API with proper HTTP methods (GET, POST, PUT, DELETE)
- Book management with full CRUD operations
- Input validation
- Standardized API responses
- Comprehensive error handling
- API documentation with Swagger/OpenAPI
- Unit and integration tests

## Technology Stack

- Java 17
- Spring Boot 3.1
- Spring Data JPA
- H2 Database (for development)
- PostgreSQL (for production)
- Maven
- JUnit 5 & Mockito
- Swagger/OpenAPI for documentation
- Lombok for reducing boilerplate code
- MapStruct for object mapping

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/sf-book-collection.git
   cd sf-book-collection
   ```

2. Build the application:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`.

### API Documentation

Once the application is running, you can access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

The OpenAPI documentation is available at:
```
http://localhost:8080/api-docs
```

## API Endpoints

### Books

| Method | Endpoint              | Description                             |
|--------|------------------------|----------------------------------------|
| GET    | /api/v1/books         | List all books                          |
| GET    | /api/v1/books/{id}    | Get book by ID                          |
| POST   | /api/v1/books         | Create a new book                       |
| PUT    | /api/v1/books/{id}    | Update an existing book                 |
| DELETE | /api/v1/books/{id}    | Delete a book                           |

## Database

The application uses H2 in-memory database for development. You can access the H2 console at:
```
http://localhost:8080/h2-console
```

Connection details (default):
- JDBC URL: `jdbc:h2:mem:sfbookdb`
- Username: `sa`
- Password: `password`

For production, configure PostgreSQL in the application.yml file.

## Testing

Run the tests with:
```
mvn test
```

## Further Development

This is Phase 1 of the implementation plan. Future phases will add:
- Author entity and management
- Collection entity and management
- Many-to-many relationships
- Pagination and filtering
- Advanced searching
- Security features

See the [Implementation Plan](docs/sf-book-implementation-plan.md) for details on future phases.

## Design Document

For a detailed overview of the API design, refer to the [Design Document](docs/sf-book-collection-design.md).

## License

This project is licensed under the MIT License - see the LICENSE file for details.