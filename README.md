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
- Spring Security with JWT authentication
- H2 Database (for development)
- PostgreSQL (for production)
- Maven
- JUnit 5 & Mockito
- Swagger/OpenAPI for documentation
- Lombok for reducing boilerplate code
- MapStruct for object mapping
- Micrometer for metrics collection and monitoring
- Actuator for health checks and monitoring

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

### Authentication

| Method | Endpoint               | Description                             | Access Control  |
|--------|------------------------|----------------------------------------|-----------------|
| POST   | /api/v1/auth/login     | Authenticate user and get JWT token    | Public          |
| POST   | /api/v1/auth/signup    | Register a new user                    | Public          |

### Books

| Method | Endpoint                   | Description                             | Access Control  |
|--------|----------------------------|----------------------------------------|-----------------|
| GET    | /api/v1/books              | List all books with pagination          | Public          |
| GET    | /api/v1/books/{id}         | Get book by ID                          | Public          |
| GET    | /api/v1/books/search       | Search books with filters               | Public          |
| GET    | /api/v1/books/authors/{id} | Get books by author                     | Public          |
| POST   | /api/v1/books              | Create a new book                       | Authenticated   |
| PUT    | /api/v1/books/{id}         | Update an existing book                 | Authenticated   |
| PATCH  | /api/v1/books/{id}         | Partially update a book                 | Authenticated   |
| DELETE | /api/v1/books/{id}         | Delete a book                           | Admin           |

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

### Running All Tests

Run all tests with:
```
mvn test
```

Note: The controller tests require proper security configuration and may fail if security settings are changed during development.

### Running Specific Tests

For more targeted testing during development:

#### Service Layer Tests Only
```
mvn test -Dtest=BookServiceImplTest,AuthorServiceImplTest,CollectionServiceImplTest
```

#### Individual Test Classes
```
mvn test -Dtest=BookServiceImplTest
mvn test -Dtest=AuthorServiceImplTest
mvn test -Dtest=CollectionServiceImplTest
```

#### Exclude Controller Tests
```
mvn test -Dtest=!*ControllerTest
```

#### Build Without Running Tests
```
mvn clean install -DskipTests
```

#### Compile Tests Without Running Them
```
mvn clean install -Dmaven.test.skip=false -DskipTests
```

## Security

The application uses JWT (JSON Web Token) for authentication and authorization:

- All list endpoints (GET) are publicly accessible
- Creating, updating and managing relationships between resources requires authentication
- Deleting resources requires ADMIN role
- Admin user is created by default (username: admin, password: admin123) in development
- Token expiration is configurable (default: 24 hours)

## Monitoring

Spring Boot Actuator endpoints are enabled for monitoring:

- `/actuator/health` - Shows application health information
- `/actuator/metrics` - Shows metrics information
- `/actuator/info` - Displays application information
- `/actuator/prometheus` - Exposes metrics in Prometheus format

## Deployment

### Environment Variables

For production deployment, the following environment variables should be set:

| Variable       | Description                                 | Default Value |
|----------------|---------------------------------------------|---------------|
| DB_HOST        | PostgreSQL database host                    | localhost     |
| DB_PORT        | PostgreSQL database port                    | 5432          |
| DB_NAME        | PostgreSQL database name                    | sfbookdb      |
| DB_USERNAME    | PostgreSQL username                         | (required)    |
| DB_PASSWORD    | PostgreSQL password                         | (required)    |
| JWT_SECRET     | Secret key for JWT token signing            | (required)    |
| PORT           | Application server port                     | 8080          |
| ENABLE_API_DOCS| Enable OpenAPI documentation                | false         |
| ENABLE_SWAGGER_UI| Enable Swagger UI                         | false         |

### Docker Deployment

Build the Docker image:
```
docker build -t sf-book-collection .
```

Run the container:
```
docker run -p 8080:8080 \
  -e DB_HOST=postgres-host \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=secret \
  -e JWT_SECRET=your-jwt-secret \
  sf-book-collection
```

See the [Implementation Plan](docs/sf-book-implementation-plan.md) for details on future phases.

## Design Document

For a detailed overview of the API design, refer to the [Design Document](docs/sf-book-collection-design.md).

## License

This project is licensed under the MIT License - see the LICENSE file for details.