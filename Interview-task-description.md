# Developer Interview Task: Flight Registration System

## Objective:
Create a REST API application that can manage flight registrations.

## Functional Requirements:

### 1. Flight Registration:
- Register a flight with details like:
  - Flight number
  - Aircraft type
  - Origin
  - Destination
  - Estimated departure time
  - Estimated arrival time

### 2. Flight Updates:
- **Status Update**:
  - Register departure: Update the actual departure time for a registered flight.
  - Register arrival: Add the arriving flight to a list awaiting terminal assignment by terminal workers.

### 3. Terminal Assignment & Concurrency:
- **Background**: Each terminal at the airport is represented by a terminal worker thread. When a flight arrives, these worker threads attempt to process the flight and assign it to their respective terminals.
- **Terminal Model**:
  - Create a model with attributes such as `terminalNumber` and `currentFlight`.
- **Terminal Worker**:
  - Spawn a terminal worker thread for every terminal in the system.
  - Each terminal worker checks a shared list for arriving flights and assigns an arriving flight to its terminal if the terminal is free.
- **Concurrency Challenge**:
  - Implement mechanisms to ensure terminals are assigned atomically and race conditions are avoided.

### 4. Flight Enquiry:
- Retrieve the status of a flight (Scheduled, Departed, Arrived, etc.)
- List all registered flights.
- Find flights by origin and/or destination.

## Technical Requirements:

### 1. Language:
- Develop the application using Java or Kotlin.

### 2. API:
- Use Spring Boot to create the RESTful API.

### 3. Database:
- Use an in-memory database like H2.

### 4. Docker:
- Create a Dockerfile to dockerize the application.

### 5. Testing:
- Write unit tests using JUnit for your services and repositories.
- Write integration tests for your controllers, especially focusing on simulating multiple arriving flights and terminal assignments. (Bonus)

### 6. Validation (Bonus):
- Validate input for flight details, ensuring attributes like flight number uniqueness and valid times.

### 7. Exception Handling:
- Return meaningful error messages and appropriate HTTP status codes for various errors.

### 8. Logging:
- Implement logging to monitor crucial actions and errors.

### 9. Security (Bonus):
- Use Spring Security to protect your API endpoints.
- Use JWT for authentication.

### 10. Documentation:
- Use tools like Swagger/OpenAPI to document your API.
- Include a `README.md` detailing steps to set up, run, and test the application.

## Submission:

Share a GitHub/Bitbucket/GitLab(any other) repository link with the source code, Dockerfile, and `README.md`. The application should run using Docker.

## Evaluation Criteria:
1. Application correctness.
2. Application of REST principles.
3. Code clarity, structure, and modularity.
4. Handling of concurrency and multithreading for terminal assignments.
5. Test quality and coverage, especially those simulating concurrency.
6. Edge case and error scenario handling.
7. Documentation quality and setup/running simplicity.
