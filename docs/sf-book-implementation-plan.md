# Science Fiction Book Collection API - Implementation Plan

This document outlines a phased approach to building the SF Book Collection API as described in the design document.

## Phase 1: Core Project Setup & Book CRUD

*   **Goal:** Establish the basic project structure and implement core CRUD functionality for the `Book` entity.
*   **Tasks:**
    *   Initialize Spring Boot project (using Spring Initializr) with necessary dependencies (Web, JPA, Lombok, Validation, Database Driver - e.g., PostgreSQL).
    *   Configure `pom.xml` or `build.gradle`.
    *   Set up basic `application.yml` (database connection, server port).
    *   Define `Book` entity, `SubGenre`, and `ReadStatus` enums.
    *   Create `BookRepository` (extends `JpaRepository`).
    *   Create initial `BookDTO` (simple representation).
    *   Create `BookService` interface and implementation with basic CRUD methods (create, get by ID, get all, update, delete).
    *   Create `BookController` with REST endpoints mapping to service methods (`POST /api/v1/books`, `GET /api/v1/books/{id}`, `GET /api/v1/books`, `PUT /api/v1/books/{id}`, `DELETE /api/v1/books/{id}`).
    *   Implement basic global exception handling (`@ControllerAdvice`).
    *   Add initial unit tests (Service layer) and integration tests (Controller layer) for Book CRUD.
    *   Integrate Swagger/OpenAPI for basic documentation.

## Phase 2: Author CRUD & Book-Author Relationship

*   **Goal:** Implement CRUD for Authors and establish the Many-to-Many relationship with Books.
*   **Tasks:**
    *   Define `Author` entity.
    *   Update `Book` and `Author` entities to define the `@ManyToMany` relationship.
    *   Create `AuthorRepository`.
    *   Create `AuthorDTO`. Update `BookDTO` to potentially include author information.
    *   Create `AuthorService` interface and implementation for Author CRUD.
    *   Create `AuthorController` with REST endpoints for Author CRUD (`POST /api/v1/authors`, `GET /api/v1/authors/{id}`, etc.).
    *   Update `BookService` and `BookController` to handle linking/unlinking authors during book creation/update.
    *   Implement MapStruct mappers for `Book` and `Author` entities/DTOs.
    *   Add unit/integration tests for Author CRUD and relationship management.
    *   Update Swagger documentation.

## Phase 3: Collection CRUD & Book-Collection Relationship

*   **Goal:** Implement CRUD for Collections and establish the Many-to-Many relationship with Books.
*   **Tasks:**
    *   Define `Collection` entity with `@ManyToMany` relationship to `Book`.
    *   Create `CollectionRepository`.
    *   Create `CollectionDTO`. Update `BookDTO` if needed (though likely not needed for this direction).
    *   Create `CollectionService` interface and implementation for Collection CRUD, including methods to add/remove books from a collection.
    *   Create `CollectionController` with REST endpoints for Collection CRUD and book management (`POST /api/v1/collections`, `GET /api/v1/collections/{id}`, `POST /api/v1/collections/{id}/books`, `DELETE /api/v1/collections/{id}/books/{bookId}`).
    *   Implement MapStruct mapper for `Collection`.
    *   Add unit/integration tests for Collection CRUD and relationship management.
    *   Update Swagger documentation.

## Phase 4: Advanced Features & Refinements

*   **Goal:** Implement pagination, sorting, searching, partial updates, and standardized responses.
*   **Tasks:**
    *   Add pagination (`Pageable`) and sorting support to all list endpoints (`GET /api/v1/books`, `GET /api/v1/authors`, `GET /api/v1/collections`).
    *   Implement search functionality (`GET /api/v1/books/search`, `GET /api/v1/authors/search` or using query parameters on list endpoints).
    *   Implement `PATCH` endpoints for `Book` (`/api/v1/books/{id}`) and `Author` (`/api/v1/authors/{id}`) entities using appropriate strategies (e.g., checking for nulls, using `JsonMergePatch`).
    *   Refine DTOs: Introduce separate Request (`BookCreateRequest`, `BookUpdateRequest`, etc.) and Response (`BookResponse`, `AuthorResponse`, etc.) DTOs. Update mappers.
    *   Implement standardized API response wrapper (including `data` and `meta` fields).
    *   Implement standardized error response format in the global exception handler.
    *   Add/update tests for new features.

## Phase 5: Security, Production Readiness & Deployment Prep

*   **Goal:** Secure the API and prepare it for deployment.
*   **Tasks:**
    *   Integrate Spring Security.
    *   Implement JWT generation, validation, and filter chain.
    *   Define User Roles (`USER`, `ADMIN`).
    *   Apply role-based authorization to endpoints (`@PreAuthorize` or similar).
    *   Configure CORS settings.
    *   Enhance input validation (`@Valid` annotations on controllers/DTOs).
    *   Configure structured logging (e.g., Logback/Log4j2).
    *   Enable and configure Spring Boot Actuator endpoints (health, metrics).
    *   Integrate Micrometer for metrics collection.
    *   Review and implement performance optimizations (database indexing based on query analysis, caching if necessary).
    *   Finalize `README.md` with setup, build, run, and API usage instructions.
    *   Create production configuration profile (`application-prod.yml`) ensuring secure secret management (environment variables, Vault, etc.).
    *   (Optional) Create a `Dockerfile` for containerization.
    *   (Optional) Set up a basic CI/CD pipeline (e.g., GitHub Actions, Jenkins).
