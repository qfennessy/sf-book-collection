# SF Book Collection - System Architecture

## Introduction

The SF Book Collection is a Spring Boot REST API application designed to manage a science fiction book collection. Its primary architectural goals include:

- Providing a robust and secure RESTful API for managing books, authors, and collections
- Implementing proper separation of concerns with a layered architecture
- Ensuring maintainability through well-structured code and clear interfaces
- Supporting authentication and authorization for secure data access

## High-Level Overview

The application follows a classic layered architecture pattern with the following main components:

```
┌─────────────────────────────────────────────────────────────┐
│                     Client Applications                      │
└───────────────────────────────┬─────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────┐
│                         REST Controllers                     │
│                                                             │
│  ┌─────────────┐   ┌──────────────┐   ┌──────────────────┐  │
│  │    Book     │   │    Author    │   │    Collection    │  │
│  │ Controller  │   │  Controller  │   │    Controller    │  │
│  └─────────────┘   └──────────────┘   └──────────────────┘  │
└───────────────────────────────┬─────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────┐
│                        Service Layer                         │
│                                                             │
│  ┌─────────────┐   ┌──────────────┐   ┌──────────────────┐  │
│  │    Book     │   │    Author    │   │    Collection    │  │
│  │   Service   │   │   Service    │   │     Service      │  │
│  └─────────────┘   └──────────────┘   └──────────────────┘  │
└───────────────────────────────┬─────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────┐
│                       Repository Layer                       │
│                                                             │
│  ┌─────────────┐   ┌──────────────┐   ┌──────────────────┐  │
│  │    Book     │   │    Author    │   │    Collection    │  │
│  │ Repository  │   │  Repository  │   │   Repository     │  │
│  └─────────────┘   └──────────────┘   └──────────────────┘  │
└───────────────────────────────┬─────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────┐
│                        Database Layer                        │
│                                                             │
│   ┌───────────────────────────────────────────────────────┐ │
│   │                        H2/PostgreSQL                   │ │
│   └───────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## Component Breakdown

### Controller Layer

The controller layer exposes the REST API endpoints and handles HTTP requests and responses. Key components include:

- **AuthController**: Manages user authentication and registration
- **BookController**: Handles CRUD operations for books
- **AuthorController**: Manages author information
- **CollectionController**: Manages book collections

Controllers are responsible for:
- Request validation
- Response formatting
- Authentication and authorization checks
- Delegating business logic to the service layer

### Service Layer

The service layer implements the core business logic of the application. Key components include:

- **BookService**: Business logic for managing books
- **AuthorService**: Business logic for managing authors
- **CollectionService**: Business logic for managing book collections
- **Authentication Services**: JWT token generation and validation

Service implementations handle:
- Data validation and business rules
- Coordinating with multiple repositories
- Implementing transactional operations
- Data transformation between DTOs and entities

### Repository Layer

The repository layer provides access to the database and encapsulates data access operations. Components include:

- **BookRepository**: Data access for book entities
- **AuthorRepository**: Data access for author entities
- **CollectionRepository**: Data access for collection entities
- **UserRepository**: Data access for user entities
- **RoleRepository**: Data access for role entities

Repositories use Spring Data JPA to provide:
- CRUD operations
- Custom query methods
- Pagination and sorting
- Transaction management

### Entity/Model Layer

The entity layer defines the data model and JPA entities:

- **Book**: Represents a book in the SF collection
- **Author**: Represents a book author
- **Collection**: Represents a user-defined collection of books
- **User**: Represents a system user
- **Role**: Represents user roles for authorization

### DTO Layer

Data Transfer Objects (DTOs) are used to transfer data between layers and to/from the client:

- **BookDTO/BookCreateDTO/BookUpdateDTO**: Book data transfer objects
- **AuthorDTO/AuthorSummaryDTO**: Author data transfer objects
- **CollectionDTO/CollectionSummaryDTO**: Collection data transfer objects
- **Authentication DTOs**: Login requests, signup requests, JWT responses

### Cross-Cutting Components

- **Security**: JWT-based authentication and role-based authorization
- **Exception Handling**: Global exception handling with standardized error responses
- **Mapping**: Object mapping between entities and DTOs using MapStruct
- **Validation**: Input validation using Bean Validation API
- **Metrics & Monitoring**: Health checks and metrics using Spring Boot Actuator

## Data Flow

### User Authentication Flow

1. Client sends login credentials to `/api/v1/auth/login`
2. AuthController validates credentials and delegates to authentication services
3. JWT token is generated and returned to the client
4. Client includes JWT token in subsequent requests
5. AuthTokenFilter validates the token for each secured endpoint

### Book Creation Flow

1. Client sends book data to `/api/v1/books` with JWT token
2. AuthTokenFilter validates the token and sets user authentication
3. BookController receives the request and validates input
4. BookService processes the request, creates the book entity, and saves it via BookRepository
5. Response is mapped to DTO and returned to the client

### Book Search Flow

1. Client sends search parameters to `/api/v1/books/search`
2. BookController receives the request and processes query parameters
3. BookService creates search criteria and queries the BookRepository
4. Results are filtered, sorted, and paginated
5. Response with matched books is returned to the client

## Design Patterns & Principles

### Architecture Patterns

- **Layered Architecture**: Clear separation of concerns with controller, service, and repository layers
- **MVC Pattern**: Controllers handle requests, models represent data, and views (JSON responses) present data
- **DTO Pattern**: Data transfer objects for API interactions
- **Repository Pattern**: Data access abstraction

### Design Principles

- **SOLID Principles**: Focus on single responsibility, interface segregation, and dependency inversion
- **DRY (Don't Repeat Yourself)**: Common functionality extracted to shared components
- **Inversion of Control**: Dependency injection using Spring Framework
- **REST Constraints**: Stateless communication, resource-based URLs, HATEOAS (partial)

## Technology Stack

- **Framework**: Spring Boot 3.1
- **API**: RESTful with JSON
- **Security**: Spring Security with JWT
- **Database Access**: Spring Data JPA with Hibernate
- **Database**: H2 (development), PostgreSQL (production)
- **Build Tool**: Maven
- **Documentation**: Swagger/OpenAPI
- **Testing**: JUnit 5, Mockito
- **Monitoring**: Spring Boot Actuator, Micrometer
- **Utility Libraries**: Lombok, MapStruct
