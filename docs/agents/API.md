# SF Book Collection - API Documentation

## Introduction

The SF Book Collection API provides a comprehensive set of endpoints for managing a science fiction book collection. It allows clients to create, read, update, and delete books, authors, and collections, with appropriate authentication and authorization controls.

## Authentication

The API uses JSON Web Tokens (JWT) for authentication.

### Obtaining Credentials

1. **Register a new user**:
   - Send a POST request to `/api/v1/auth/signup`
   - Include username, email, password, and optional roles in the request body
   
2. **Log in to get a JWT token**:
   - Send a POST request to `/api/v1/auth/login`
   - Include username and password in the request body
   - The response will contain a JWT token

### Using Authentication

1. For endpoints requiring authentication, include the JWT token in the Authorization header:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

2. The token expires after 24 hours (configurable), after which you need to log in again.

## Base URLs

- **Development**: `http://localhost:8080`
- **Production**: `https://[your-production-domain]/`

## Rate Limiting

Currently, the API does not implement rate limiting. However, plans for future enhancements may include rate limiting to protect the service from abuse.

## Error Handling

The API uses standard HTTP status codes and provides detailed error messages in the response body.

### Standard Error Response Format

```json
{
  "code": "ERROR_CODE",
  "message": "A descriptive error message",
  "timestamp": "2025-04-21T12:34:56.789Z",
  "path": "/api/v1/endpoint"
}
```

### Common Error Codes

- **400 Bad Request**: Invalid input or validation errors
- **401 Unauthorized**: Missing or invalid authentication
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server-side error

## Endpoint Definitions

### Authentication Endpoints

#### Register a New User

- **URL**: `/api/v1/auth/signup`
- **Method**: `POST`
- **Description**: Register a new user in the system
- **Authentication**: None
- **Request Body**:
  ```json
  {
    "username": "string",
    "email": "string",
    "password": "string",
    "roles": ["user", "admin"] // Optional
  }
  ```
- **Success Response**:
  - **Code**: 200
  - **Content**:
    ```json
    {
      "status": "success",
      "data": {
        "message": "User registered successfully!"
      }
    }
    ```
- **Error Responses**:
  - **Code**: 400
  - **Content**: Username/email already taken

#### Login

- **URL**: `/api/v1/auth/login`
- **Method**: `POST`
- **Description**: Authenticate user and get JWT token
- **Authentication**: None
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Success Response**:
  - **Code**: 200
  - **Content**:
    ```json
    {
      "status": "success",
      "data": {
        "token": "string",
        "type": "Bearer",
        "id": "number",
        "username": "string",
        "email": "string",
        "roles": ["string"]
      }
    }
    ```
- **Error Responses**:
  - **Code**: 401
  - **Content**: Invalid credentials

### Book Endpoints

#### Get All Books

- **URL**: `/api/v1/books`
- **Method**: `GET`
- **Description**: Retrieve a paginated list of books
- **Authentication**: None (Public)
- **Query Parameters**:
  - `page` (integer, optional): Page number (0-indexed, default 0)
  - `size` (integer, optional): Page size (default 10)
  - `sort` (string, optional): Sort field (default "id,asc")
- **Success Response**:
  - **Code**: 200
  - **Content**:
    ```json
    {
      "status": "success",
      "data": {
        "content": [
          {
            "id": "number",
            "title": "string",
            "synopsis": "string",
            "publicationYear": "number",
            "coverImageUrl": "string",
            "isbn": "string",
            "pageCount": "number",
            "subGenre": "string",
            "author": {
              "id": "number",
              "name": "string"
            }
          }
        ],
        "pageMeta": {
          "page": "number",
          "size": "number",
          "totalElements": "number",
          "totalPages": "number"
        }
      }
    }
    ```

#### Get Book by ID

- **URL**: `/api/v1/books/{id}`
- **Method**: `GET`
- **Description**: Retrieve details of a specific book
- **Authentication**: None (Public)
- **Path Parameters**:
  - `id` (integer): Book ID
- **Success Response**:
  - **Code**: 200
  - **Content**:
    ```json
    {
      "status": "success",
      "data": {
        "id": "number",
        "title": "string",
        "synopsis": "string",
        "publicationYear": "number",
        "coverImageUrl": "string",
        "isbn": "string",
        "pageCount": "number",
        "subGenre": "string",
        "author": {
          "id": "number",
          "name": "string"
        }
      }
    }
    ```
- **Error Responses**:
  - **Code**: 404
  - **Content**: Book not found

#### Search Books

- **URL**: `/api/v1/books/search`
- **Method**: `GET`
- **Description**: Search books based on various criteria
- **Authentication**: None (Public)
- **Query Parameters**:
  - `title` (string, optional): Book title (partial match)
  - `author` (string, optional): Author name (partial match)
  - `subGenre` (string, optional): Book sub-genre
  - `page` (integer, optional): Page number (0-indexed, default 0)
  - `size` (integer, optional): Page size (default 10)
- **Success Response**:
  - **Code**: 200
  - **Content**: Same as Get All Books

#### Create Book

- **URL**: `/api/v1/books`
- **Method**: `POST`
- **Description**: Create a new book
- **Authentication**: Required
- **Request Body**:
  ```json
  {
    "title": "string",
    "synopsis": "string",
    "publicationYear": "number",
    "coverImageUrl": "string",
    "isbn": "string",
    "pageCount": "number",
    "subGenre": "string",
    "authorId": "number"
  }
  ```
- **Success Response**:
  - **Code**: 201
  - **Content**: Created book details
- **Error Responses**:
  - **Code**: 400
  - **Content**: Validation errors
  - **Code**: 401
  - **Content**: Unauthorized
  - **Code**: 404
  - **Content**: Author not found

#### Update Book

- **URL**: `/api/v1/books/{id}`
- **Method**: `PUT`
- **Description**: Update an existing book
- **Authentication**: Required
- **Path Parameters**:
  - `id` (integer): Book ID
- **Request Body**: Same as Create Book
- **Success Response**:
  - **Code**: 200
  - **Content**: Updated book details
- **Error Responses**:
  - **Code**: 400
  - **Content**: Validation errors
  - **Code**: 401
  - **Content**: Unauthorized
  - **Code**: 404
  - **Content**: Book not found

#### Partially Update Book

- **URL**: `/api/v1/books/{id}`
- **Method**: `PATCH`
- **Description**: Partially update an existing book
- **Authentication**: Required
- **Path Parameters**:
  - `id` (integer): Book ID
- **Request Body**: Fields to update (any subset of the Book schema)
- **Success Response**:
  - **Code**: 200
  - **Content**: Updated book details
- **Error Responses**: Same as Update Book

#### Delete Book

- **URL**: `/api/v1/books/{id}`
- **Method**: `DELETE`
- **Description**: Delete a book
- **Authentication**: Required (Admin only)
- **Path Parameters**:
  - `id` (integer): Book ID
- **Success Response**:
  - **Code**: 204
  - **Content**: No content
- **Error Responses**:
  - **Code**: 401
  - **Content**: Unauthorized
  - **Code**: 403
  - **Content**: Forbidden (non-admin user)
  - **Code**: 404
  - **Content**: Book not found

### Author Endpoints

#### Get All Authors

- **URL**: `/api/v1/authors`
- **Method**: `GET`
- **Description**: Retrieve a paginated list of authors
- **Authentication**: None (Public)
- **Query Parameters**:
  - `page` (integer, optional): Page number (0-indexed, default 0)
  - `size` (integer, optional): Page size (default 10)
- **Success Response**:
  - **Code**: 200
  - **Content**:
    ```json
    {
      "status": "success",
      "data": {
        "content": [
          {
            "id": "number",
            "name": "string",
            "biography": "string",
            "birthDate": "string (ISO date)",
            "deathDate": "string (ISO date, optional)",
            "photoUrl": "string"
          }
        ],
        "pageMeta": {
          "page": "number",
          "size": "number",
          "totalElements": "number",
          "totalPages": "number"
        }
      }
    }
    ```

#### Get Author by ID

- **URL**: `/api/v1/authors/{id}`
- **Method**: `GET`
- **Description**: Retrieve a specific author with their books
- **Authentication**: None (Public)
- **Path Parameters**:
  - `id` (integer): Author ID
- **Success Response**:
  - **Code**: 200
  - **Content**:
    ```json
    {
      "status": "success",
      "data": {
        "id": "number",
        "name": "string",
        "biography": "string",
        "birthDate": "string (ISO date)",
        "deathDate": "string (ISO date, optional)",
        "photoUrl": "string",
        "books": [
          {
            "id": "number",
            "title": "string"
          }
        ]
      }
    }
    ```
- **Error Responses**:
  - **Code**: 404
  - **Content**: Author not found

#### Create Author

- **URL**: `/api/v1/authors`
- **Method**: `POST`
- **Description**: Create a new author
- **Authentication**: Required
- **Request Body**:
  ```json
  {
    "name": "string",
    "biography": "string",
    "birthDate": "string (ISO date)",
    "deathDate": "string (ISO date, optional)",
    "photoUrl": "string"
  }
  ```
- **Success Response**:
  - **Code**: 201
  - **Content**: Created author details
- **Error Responses**:
  - **Code**: 400
  - **Content**: Validation errors
  - **Code**: 401
  - **Content**: Unauthorized

#### Update Author

- **URL**: `/api/v1/authors/{id}`
- **Method**: `PUT`
- **Description**: Update an existing author
- **Authentication**: Required
- **Path Parameters**:
  - `id` (integer): Author ID
- **Request Body**: Same as Create Author
- **Success Response**:
  - **Code**: 200
  - **Content**: Updated author details
- **Error Responses**:
  - **Code**: 400
  - **Content**: Validation errors
  - **Code**: 401
  - **Content**: Unauthorized
  - **Code**: 404
  - **Content**: Author not found

#### Delete Author

- **URL**: `/api/v1/authors/{id}`
- **Method**: `DELETE`
- **Description**: Delete an author
- **Authentication**: Required (Admin only)
- **Path Parameters**:
  - `id` (integer): Author ID
- **Success Response**:
  - **Code**: 204
  - **Content**: No content
- **Error Responses**:
  - **Code**: 401
  - **Content**: Unauthorized
  - **Code**: 403
  - **Content**: Forbidden (non-admin user)
  - **Code**: 404
  - **Content**: Author not found
  - **Code**: 400
  - **Content**: Cannot delete author with books

### Collection Endpoints

#### Get All Collections

- **URL**: `/api/v1/collections`
- **Method**: `GET`
- **Description**: Retrieve a paginated list of public collections
- **Authentication**: None (Public)
- **Query Parameters**:
  - `page` (integer, optional): Page number (0-indexed, default 0)
  - `size` (integer, optional): Page size (default 10)
- **Success Response**:
  - **Code**: 200
  - **Content**:
    ```json
    {
      "status": "success",
      "data": {
        "content": [
          {
            "id": "number",
            "name": "string",
            "description": "string",
            "isPublic": "boolean",
            "userId": "number",
            "bookCount": "number"
          }
        ],
        "pageMeta": {
          "page": "number",
          "size": "number",
          "totalElements": "number",
          "totalPages": "number"
        }
      }
    }
    ```

#### Get Collection by ID

- **URL**: `/api/v1/collections/{id}`
- **Method**: `GET`
- **Description**: Retrieve a specific collection with its books
- **Authentication**: Required for private collections
- **Path Parameters**:
  - `id` (integer): Collection ID
- **Success Response**:
  - **Code**: 200
  - **Content**:
    ```json
    {
      "status": "success",
      "data": {
        "id": "number",
        "name": "string",
        "description": "string",
        "isPublic": "boolean",
        "userId": "number",
        "books": [
          {
            "id": "number",
            "title": "string",
            "author": {
              "id": "number",
              "name": "string"
            }
          }
        ]
      }
    }
    ```
- **Error Responses**:
  - **Code**: 404
  - **Content**: Collection not found
  - **Code**: 403
  - **Content**: Access to private collection denied

#### Create Collection

- **URL**: `/api/v1/collections`
- **Method**: `POST`
- **Description**: Create a new collection
- **Authentication**: Required
- **Request Body**:
  ```json
  {
    "name": "string",
    "description": "string",
    "isPublic": "boolean",
    "bookIds": ["number"]
  }
  ```
- **Success Response**:
  - **Code**: 201
  - **Content**: Created collection details
- **Error Responses**:
  - **Code**: 400
  - **Content**: Validation errors
  - **Code**: 401
  - **Content**: Unauthorized

#### Update Collection

- **URL**: `/api/v1/collections/{id}`
- **Method**: `PUT`
- **Description**: Update an existing collection
- **Authentication**: Required (collection owner only)
- **Path Parameters**:
  - `id` (integer): Collection ID
- **Request Body**: Same as Create Collection
- **Success Response**:
  - **Code**: 200
  - **Content**: Updated collection details
- **Error Responses**:
  - **Code**: 400
  - **Content**: Validation errors
  - **Code**: 401
  - **Content**: Unauthorized
  - **Code**: 403
  - **Content**: Not the collection owner
  - **Code**: 404
  - **Content**: Collection not found

#### Delete Collection

- **URL**: `/api/v1/collections/{id}`
- **Method**: `DELETE`
- **Description**: Delete a collection
- **Authentication**: Required (collection owner or admin)
- **Path Parameters**:
  - `id` (integer): Collection ID
- **Success Response**:
  - **Code**: 204
  - **Content**: No content
- **Error Responses**:
  - **Code**: 401
  - **Content**: Unauthorized
  - **Code**: 403
  - **Content**: Not the collection owner or admin
  - **Code**: 404
  - **Content**: Collection not found

#### Add Book to Collection

- **URL**: `/api/v1/collections/{collectionId}/books/{bookId}`
- **Method**: `POST`
- **Description**: Add a book to a collection
- **Authentication**: Required (collection owner only)
- **Path Parameters**:
  - `collectionId` (integer): Collection ID
  - `bookId` (integer): Book ID
- **Success Response**:
  - **Code**: 200
  - **Content**: Updated collection details
- **Error Responses**:
  - **Code**: 401
  - **Content**: Unauthorized
  - **Code**: 403
  - **Content**: Not the collection owner
  - **Code**: 404
  - **Content**: Collection or book not found
  - **Code**: 400
  - **Content**: Book already in collection

#### Remove Book from Collection

- **URL**: `/api/v1/collections/{collectionId}/books/{bookId}`
- **Method**: `DELETE`
- **Description**: Remove a book from a collection
- **Authentication**: Required (collection owner only)
- **Path Parameters**:
  - `collectionId` (integer): Collection ID
  - `bookId` (integer): Book ID
- **Success Response**:
  - **Code**: 200
  - **Content**: Updated collection details
- **Error Responses**:
  - **Code**: 401
  - **Content**: Unauthorized
  - **Code**: 403
  - **Content**: Not the collection owner
  - **Code**: 404
  - **Content**: Collection or book not found
  - **Code**: 400
  - **Content**: Book not in collection