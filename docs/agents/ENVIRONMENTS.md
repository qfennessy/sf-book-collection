# SF Book Collection - Environments and Deployment

## Overview

The SF Book Collection application supports multiple deployment environments to facilitate development, testing, and production use. Each environment is configured with appropriate settings for its specific purpose.

The following environments are supported:

1. **Local Development**: For individual developers to work on the application
2. **Testing/QA**: For automated tests and quality assurance
3. **Staging**: For pre-production verification
4. **Production**: For end-user access

## Local Development Setup

### Prerequisites

- Java JDK 17 or higher
- Maven 3.6 or higher
- Git
- IDE (IntelliJ IDEA, Eclipse, VS Code with Java extensions, etc.)
- Postman (optional, for API testing)

### Repository Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/sf-book-collection.git
   cd sf-book-collection
   ```

2. Install dependencies:
   ```bash
   mvn clean install
   ```

### Configuration

The application uses Spring profiles to manage environment-specific configurations:

1. **application.yml**: Common configuration shared across all environments
2. **application-dev.yml**: Development-specific configuration
3. **application-prod.yml**: Production-specific configuration

Key configuration properties include:

- **Database**: H2 in-memory database for development, PostgreSQL for production
- **JWT Secret**: Secret key for JWT token generation (configurable via environment variables)
- **Logging**: Debug level for development, INFO level for production

#### Environment Variables

You can override configuration values using environment variables:

| Variable    | Description                     | Default Value                     |
|-------------|---------------------------------|-----------------------------------|
| PORT        | Server port                     | 8080                              |
| JWT_SECRET  | Secret key for JWT tokens       | changeme_in_production...         |
| DB_HOST     | Database host                   | localhost                         |
| DB_PORT     | Database port                   | 5432                              |
| DB_NAME     | Database name                   | sfbookdb                          |
| DB_USERNAME | Database username               | (Development: sa)                 |
| DB_PASSWORD | Database password               | (Development: password)           |

### Running the Application Locally

Run the application using Maven:

```bash
mvn spring-boot:run
```

This starts the application with the default development profile. To use a specific profile:

```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

### Accessing the Application

- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console** (development only): http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:sfbookdb`
  - Username: `sa`
  - Password: `password`

### Running Tests Locally

```bash
# Run all tests
mvn test

# Run specific test classes
mvn test -Dtest=BookServiceImplTest,AuthorServiceImplTest

# Skip tests
mvn clean install -DskipTests
```

## Testing/QA Environment

The testing/QA environment is used for automated tests and quality assurance.

### Purpose

- Run automated tests (unit, integration, and end-to-end)
- Verify functionality in a clean environment
- Test with fresh databases for consistent results

### Configuration

- Uses an H2 in-memory database reset for each test suite
- Controlled test data inserted via SQL scripts
- Security configured for testing authentication and authorization

### Access Details

- Typically not accessible outside CI/CD pipelines
- Tests run automatically on pull requests and commits to main branches

## Staging Environment

The staging environment mirrors the production environment as closely as possible, allowing for pre-release testing.

### Purpose

- Validate deployment process
- Test with production-like data
- Verify performance and security
- User acceptance testing (UAT)

### Configuration

- Uses PostgreSQL database
- Production-comparable hardware resources
- Complete security measures

### Access Details

- URL: https://staging.sfbookcollection.com (example)
- Access restricted to internal team and authorized testers
- JWT tokens required for most endpoints

### Key Configuration Differences from Production

- Enhanced logging for debugging
- Test data instead of real user data
- Rate limits and quotas may be more lenient

## Production Environment

The production environment hosts the live application for end users.

### Purpose

- Serve actual users
- Maintain high availability and performance
- Protect real user data

### Configuration

- PostgreSQL database with backups
- Optimized for performance
- Strict security measures
- Minimal logging (only essential information)

### Access Details

- URL: https://sfbookcollection.com (example)
- Public access to read endpoints
- Authenticated access for write operations
- Admin access restricted to authorized personnel

## Deployment Process

### CI/CD Pipeline

The application uses a CI/CD pipeline for automated testing and deployment:

1. **Code Changes**:
   - Developer creates a branch and implements changes
   - Pull request created for review

2. **Continuous Integration**:
   - Automated tests run on pull request
   - Code quality checks (linting, static analysis)
   - Security scanning

3. **Continuous Deployment**:
   - Automatically deploy to staging after PR merge to main branch
   - Manual approval for production deployment

### Manual Deployment Steps

If CI/CD is not available, follow these steps for manual deployment:

1. Build the application:
   ```bash
   mvn clean package -DskipTests
   ```

2. Copy the JAR file to the server:
   ```bash
   scp target/sf-book-collection-0.1.0-SNAPSHOT.jar user@server:/path/to/deployment/
   ```

3. Set environment variables on the server (production):
   ```bash
   export DB_HOST=db.example.com
   export DB_USERNAME=produser
   export DB_PASSWORD=securepassword
   export JWT_SECRET=your-secure-jwt-secret
   ```

4. Run the application:
   ```bash
   java -jar sf-book-collection-0.1.0-SNAPSHOT.jar --spring.profiles.active=prod
   ```

### Docker Deployment

The application can also be deployed using Docker:

1. Build the Docker image:
   ```bash
   docker build -t sf-book-collection .
   ```

2. Run the container:
   ```bash
   docker run -p 8080:8080 \
     -e DB_HOST=db.example.com \
     -e DB_USERNAME=produser \
     -e DB_PASSWORD=securepassword \
     -e JWT_SECRET=your-secure-jwt-secret \
     sf-book-collection
   ```

### Health Checks

Once deployed, verify the application health:

- `/actuator/health`: Returns the application health status
- `/actuator/info`: Returns application information
- `/actuator/metrics`: Returns application metrics

## Database Management

### Development Database

- H2 in-memory database
- Schema created automatically
- Data loaded from `import.sql`
- Accessible via H2 Console at `/h2-console`

### Production Database

- PostgreSQL database
- Migrations managed through JPA and SQL scripts
- Regular backups scheduled
- Database connection pooling configured for optimal performance