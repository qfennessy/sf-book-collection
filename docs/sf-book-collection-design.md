# Science Fiction Book Collection API Design

## Overview
This document outlines the design for a RESTful API service built with Spring Boot to manage a science fiction book collection. The API will provide CRUD (Create, Read, Update, Delete) operations for books, authors, and collections.

## Domain Model

### Book
- `id`: Long (Primary Key)
- `title`: String (required)
- `isbn`: String (unique)
- `publishedDate`: LocalDate
- `description`: String
- `coverImage`: String (URL to image)
- `authors`: List<Author> (Many-to-Many)
- `subGenre`: SubGenre (Enum)
- `pageCount`: Integer
- `publisher`: String
- `language`: String
- `rating`: Float (1-5)
- `readStatus`: ReadStatus (Enum)
- `dateAdded`: LocalDateTime

### Author
- `id`: Long (Primary Key)
- `name`: String (required)
- `biography`: String
- `birthDate`: LocalDate
- `books`: List<Book> (Many-to-Many)
- `photoUrl`: String (URL to image)

### Collection
- `id`: Long (Primary Key)
- `name`: String (required)
- `description`: String
- `books`: List<Book> (Many-to-Many)
- `dateCreated`: LocalDateTime
- `lastModified`: LocalDateTime

### Enums
- **SubGenre**: SPACE_OPERA, CYBERPUNK, DYSTOPIAN, POST_APOCALYPTIC, HARD_SF, FIRST_CONTACT, TIME_TRAVEL, ALTERNATE_HISTORY, etc.
- **ReadStatus**: TO_READ, READING, COMPLETED, ABANDONED

## API Endpoints

### Books

| Method | Endpoint                   | Description                                  |
|--------|----------------------------|----------------------------------------------|
| GET    | /api/v1/books              | List all books (with pagination and filters) |
| GET    | /api/v1/books/{id}         | Get book by ID                               |
| POST   | /api/v1/books              | Create a new book                            |
| PUT    | /api/v1/books/{id}         | Update an existing book                      |
| PATCH  | /api/v1/books/{id}         | Partially update an existing book            |
| DELETE | /api/v1/books/{id}         | Delete a book                                |
| GET    | /api/v1/books/search       | Search books by criteria (Alternative: Query params on /api/v1/books) |

### Authors

| Method | Endpoint                   | Description                                  |
|--------|----------------------------|----------------------------------------------|
| GET    | /api/v1/authors            | List all authors (with pagination)           |
| GET    | /api/v1/authors/{id}       | Get author by ID                             |
| POST   | /api/v1/authors            | Create a new author                          |
| PUT    | /api/v1/authors/{id}       | Update an existing author                    |
| PATCH  | /api/v1/authors/{id}       | Partially update an existing author          |
| DELETE | /api/v1/authors/{id}       | Delete an author                             |
| GET    | /api/v1/authors/search     | Search authors by name (Alternative: Query params on /api/v1/authors) |

### Collections

| Method | Endpoint                      | Description                                  |
|--------|-----------------------------|----------------------------------------------|
| GET    | /api/v1/collections           | List all collections                         |
| GET    | /api/v1/collections/{id}      | Get collection by ID                         |
| POST   | /api/v1/collections           | Create a new collection                      |
| PUT    | /api/v1/collections/{id}      | Update an existing collection                |
| DELETE | /api/v1/collections/{id}      | Delete a collection                          |
| POST   | /api/v1/collections/{id}/books| Add books to collection (Accepts a list of book IDs in the request body) |
| DELETE | /api/v1/collections/{id}/books/{bookId} | Remove book from collection        |

## Architecture

### Project Structure
```
sf-book-collection/
├── src/
│   ├── main/
│   │   ├── java/com/sfcollection/
│   │   │   ├── SfBookCollectionApplication.java
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application-prod.yml
│   └── test/
│       └── java/com/sfcollection/
│           ├── controller/
│           ├── repository/
│           └── service/
└── pom.xml
```

### Technical Components

#### Controllers
RESTful controllers to handle HTTP requests/responses, input validation, and response formatting.

#### DTOs (Data Transfer Objects)
Objects for data exchange between client and server, separating domain models from API representation. Consider separate Request (e.g., `BookCreateRequest`, `BookUpdateRequest`) and Response (e.g., `BookResponse`) DTOs for fine-grained control.

#### Mappers
Using MapStruct for object mapping between DTOs and entities.

#### Services
Business logic implementation.

#### Repositories
JPA repositories for data access.

#### Exception Handling
Global exception handler to provide consistent error responses.

## Best Practices Implementation

### API Versioning
Using URI versioning (v1) to support backward compatibility for future changes.

### Response Format
Standardized JSON response format:
```json
{
  "data": { /* response data */ },
  "meta": {
    "timestamp": "2023-07-10T15:30:45Z",
    "page": 1,
    "size": 10,
    "totalElements": 100,
    "totalPages": 10
  }
}
```

### Error Handling
Consistent error response format:
```json
{
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "Book with ID 123 not found",
    "timestamp": "2023-07-10T15:30:45Z",
    "path": "/api/v1/books/123"
  }
}
```

### Security
- JWT-based authentication
- Role-based authorization (USER, ADMIN - e.g., ADMIN required for DELETE operations, USER for others)
- Input validation to prevent injection attacks
- CORS configuration
- Secure management of secrets (e.g., via environment variables or configuration server)

### Pagination and Filtering
- Support for page and size parameters
- Sorting capability (sortBy, direction)
- Filtering by different attributes

### Documentation
- Swagger/OpenAPI for API documentation
- Postman collection for testing

### Testing
- Unit tests for services and repositories
- Integration tests for controllers
- Test coverage reporting

### Performance Optimization
- Response caching for frequently accessed resources
- Lazy loading for related entities
- Optimized queries with pagination
- Consider database indexes on frequently queried fields (e.g., `book.title`, `author.name`, `book.isbn`)

### Logging and Monitoring
- Structured logging using SLF4J
- Metrics collection using Micrometer
- Health endpoints for monitoring

## Database Schema

```
+-------------+       +----------------+       +--------------+
|   BOOK      |       | BOOK_AUTHOR    |       |  AUTHOR      |
+-------------+       +----------------+       +--------------+
| id          |<----->| book_id        |<----->| id           |
| title       |       | author_id      |       | name         |
| isbn        |       +----------------+       | biography    |
| publishedDate |
| description   |
| coverImage    |
| subGenre      |
| pageCount     |
| publisher     |
| language      |
| rating        |
| readStatus    |
| dateAdded     |
+-------------+       +-------------------+       +--------------+
      ^               | COLLECTION_BOOK   |       | COLLECTION   |
      |               +-------------------+       +--------------+
      +---------------| book_id           |<----->| id           |
                      | collection_id     |       | name         |
                      +-------------------+       | description  |
                                                      | dateCreated  |
                                                      | lastModified |
                                                      +--------------+
```

## Technology Stack
- Java (Version specified in pom.xml)
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL (or other relational database)
- Maven
- JUnit 5 & Mockito
- MapStruct
- Lombok
- Swagger/OpenAPI

## Future Considerations
- Integration with external book data sources (e.g., Open Library API)
- User reviews and ratings system
- Recommendation engine
- More advanced search capabilities (Full-text search)
- User profiles and settings