# Generic Documentation Generation Prompt

**Objective:** Review the current codebase in depth and use that information to create  documentation, covering Architecture, API, Environments, and Testing. Generate four separate markdown files and store them in the `docs/agents` directory: `ARCHITECTURE.md`, `API.md`, `ENVIRONMENTS.md`, and `TESTING.md`. The content should be structured clearly and provide a solid starting point for project documentation, .

**Instructions:**

For each file, generate content based on the following guidelines. Use standard markdown formatting. Assume a generic software project unless specific details are provided.

## 1. `ARCHITECTURE.md` - System Architecture

*   **Introduction:** Briefly describe the project's purpose and the overall architectural goals (e.g., scalability, maintainability).
*   **High-Level Overview:** Provide a diagram or description of the main components/layers (e.g., Frontend, Backend API, Database, Message Queue, External Services).
*   **Component Breakdown:** Detail each major component/layer, explaining its responsibilities and key technologies (if known, otherwise use placeholders like "[Technology Used]").
*   **Data Flow:** Describe how data moves through the system for key user interactions or processes.
*   **Design Patterns & Principles:** Mention any core architectural patterns (e.g., Microservices, Monolith, MVC, Layered) or design principles followed.
*   **Technology Stack (Optional):** List key technologies if provided, otherwise leave placeholders.

## 2. `API.md` - API Documentation (Assume RESTful or similar)

*   **Introduction:** Overview of the API's purpose and how to consume it.
*   **Authentication:** Describe the authentication mechanism (e.g., API Keys, JWT, OAuth). Include details on how to obtain and use credentials. Use placeholders if specifics are unknown.
*   **Base URL:** Specify the base URL(s) for different environments (e.g., Dev, Staging, Prod).
*   **Rate Limiting:** Explain any rate limits.
*   **Error Handling:** Describe the standard error response format (status codes, error message structure).
*   **Endpoint Definitions:** For *each major resource/functionality*:
    *   HTTP Method (GET, POST, PUT, DELETE, etc.) & Path (e.g., `/users`, `/products/{id}`)
    *   Description
    *   Request Parameters (path, query, header, body). Specify data types and requirements. Use placeholders like `[SchemaName]`.
    *   Example Request
    *   Success Response (status code, body structure). Use placeholders like `[SchemaName]`.
    *   Example Success Response
    *   Error Responses (possible codes and meanings).

## 3. `ENVIRONMENTS.md` - Environments and Deployment

*   **Overview:** List the different deployment environments (e.g., Local Development, Testing/QA, Staging, Production).
*   **Local Development Setup:**
    *   Prerequisites (e.g., `[Runtime Version]`, `[Package Manager]`, Docker).
    *   Repository cloning steps.
    *   Dependency installation (e.g., `[Command]`).
    *   Configuration (e.g., environment variables, `.env` files). Explain key variables.
    *   How to run the application locally.
    *   How to run tests locally.
*   **Other Environments (Testing/QA, Staging, Production):**
    *   Purpose of each.
    *   Access details (URLs, credential handling).
    *   Key configuration differences.
    *   Deployment process overview (e.g., CI/CD, manual steps).

## 4. `TESTING.md` - Testing Strategy

*   **Philosophy/Strategy:** Project's approach to testing (e.g., Test Pyramid).
*   **Types of Tests:**
    *   Unit Tests (Scope, Tools/Frameworks).
    *   Integration Tests (Scope, How run, Dependencies).
    *   End-to-End (E2E) Tests (Scope, Tools/Frameworks, Setup).
    *   (Optional: Performance, Security, etc.)
*   **Running Tests:**
    *   Command(s) to run all tests.
    *   Command(s) to run specific tests/types.
    *   Test environment setup (e.g., test database).
*   **Test Coverage:** Measurement and reporting (tool, standards).
*   **Writing Tests:** Guidelines or conventions.

**Final Note:** Use clear, concise, neutral language. Employ placeholders like `[Your Project Name]`, `[Specific Technology]`, `[Example Schema]` where needed.