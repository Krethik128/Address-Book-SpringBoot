# Address Book REST API

A robust Spring Boot application for managing user contacts and their associated addresses. This project demonstrates a user-centric data model with proper JPA relationships, DTOs, validation, logging, and centralized exception handling.

##  Features

### User Management:
- Create, retrieve, update, and delete users.
- Each user can have multiple addresses.

### Address Management:
- Addresses are intrinsically linked to a user.
- Create, update, and delete addresses as part of user operations.
- Retrieve all addresses globally via a dedicated endpoint.
- Retrieve a specific address by its ID.

### Search & Sort:
- Search for users based on address city, state, or a combination (AND/OR).
- Addresses in search results are sorted by city in ascending order.
- Retrieve all users sorted by the city or state of their primary address.

### Additional Features:
- **Data Transfer Objects (DTOs)**: Separate DTOs for request (input) and response (output) payloads to control API contract and prevent infinite recursion in bidirectional relationships.
- **Validation**: Utilizes jakarta.validation annotations for robust input validation on DTOs.
- **Centralized Exception Handling**: Custom exceptions and a global exception handler (@ControllerAdvice) provide consistent and informative error responses.
- **Logging**: Comprehensive logging using SLF4J and Logback for debugging and monitoring.
- **Profile-Based Configuration**: Supports different environment configurations (e.g., dev, prod, log) for database connections and logging levels.
- **Secure Credential Handling**: Database credentials are managed via environment variables for enhanced security.

##  Technologies Used

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Hibernate**
- **MySQL Database**
- **Lombok**: Reduces boilerplate code for POJOs.
- **Jackson**: For JSON serialization/deserialization.
- **jakarta.validation**: For declarative data validation.
- **Maven**: Build automation tool.

##  Project Structure

```
src/main/java/com/gevernova/addressbook/
├── controller/          # REST API Controllers (e.g., UserController, AddressController)
├── dto/                 # Data Transfer Objects (Request/Response for entities)
├── entity/              # JPA Entities (e.g., User, Address)
├── exceptionhandler/    # Custom Exceptions (e.g., UserNotFoundException, AddressNotFoundException)
├── mapper/              # Mapper classes for Entity <-> DTO conversion
├── repository/          # Spring Data JPA Repositories
└── service/             # Business Logic Services

src/main/resources/
├── application.properties     # Base properties
├── application-dev.properties # Development profile properties
├── application-prod.properties# Production profile properties
├── application-log.properties # Log profile properties
```

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.6+
- MySQL Server running on localhost:3306 (or your configured host/port)
- A MySQL database named `employee_db` (or as configured in your properties)


### 1. Database Setup (MySQL)

Ensure your MySQL server is running. The application is configured to connect to a database named `employee_db` on `localhost:3306`. If this database doesn't exist, Spring Boot will attempt to create it upon startup if `createDatabaseIfNotExist=true` is set in your JDBC URL.

**Recommended steps:**

1. Log in to your MySQL server (e.g., using MySQL Workbench or command line).
2. If `employee_db` doesn't exist, create it:
   ```sql
   CREATE DATABASE IF NOT EXISTS employee_db;
   ```

### 2. Environment Variables for Database Credentials

To keep your database credentials secure, they are read from environment variables.

Set the following environment variables before running the application:

- `DB_USERNAME`: Your MySQL username (e.g., root, dev_user)
- `DB_PASSWORD`: Your MySQL password

### 3. Running the Backend

You can run the application using Maven:

The application will start on `http://localhost:8080`.

##  API Endpoints

All API endpoints are prefixed with `/api`.

### User Management (`/api/users`)

#### POST `/api/users`
- **Description**: Create a new user with associated addresses.
- **Body (JSON)**:
  ```json
  {
    "firstName": "Alice",
    "lastName": "Smith",
    "phoneNumber": "555-111-2222",
    "email": "alice.smith@example.com",
    "addresses": [
      {
        "street": "101 Maple St",
        "addressLine2": "Apt 1",
        "city": "Springfield",
        "state": "IL",
        "zipCode": "62701",
        "country": "USA",
        "tags": ["home", "primary"]
      }
    ]
  }
  ```
- **Response**: 201 Created with the created UserResponseDTO.

#### GET `/api/users`
- **Description**: Retrieve all users.
- **Response**: 200 OK with a list of UserResponseDTO.

#### GET `/api/users/{id}`
- **Description**: Retrieve a user by ID.
- **Path Variable**: `{id}` (e.g., 1)
- **Response**: 200 OK with UserResponseDTO or 404 Not Found.

#### PUT `/api/users/{id}`
- **Description**: Update an existing user. Addresses in the body (with their ids) will be updated; new addresses (without ids) will be created; addresses omitted from the list (if orphanRemoval=true is set on the @OneToMany) will be deleted.
- **Path Variable**: `{id}`
- **Body (JSON)**: Same structure as POST, but include id for addresses if updating.
- **Response**: 200 OK with updated UserResponseDTO or 404 Not Found.

#### DELETE `/api/users/{id}`
- **Description**: Delete a user and all their associated addresses.
- **Path Variable**: `{id}`
- **Response**: 204 No Content or 404 Not Found.

### Address Search & Sort (`/api/users` or `/api/addresses`)

#### GET `/api/users/search-by-address`
- **Description**: Search for users whose addresses match the given city AND/OR state. Addresses in the response are implicitly sorted by city ascending via native queries.
- **Query Parameters**:
    - `city` (optional): Partial or full city name.
    - `state` (optional): Partial or full state name.
- **Examples**:
    - `GET /api/users/search-by-address?city=Springfield&state=IL` (City AND State)
    - `GET /api/users/search-by-address?city=Metropolis` (Only City)
    - `GET /api/users/search-by-address?state=NY` (Only State)
    - `GET /api/users/search-by-address` (Returns all users if no parameters)
- **Response**: 200 OK with a list of UserResponseDTOs.

#### GET `/api/users/search-by-address-or`
- **Description**: Search for users whose addresses match the given city OR state. Addresses in the response are implicitly sorted by city ascending via native queries.
- **Query Parameters**:
    - `city` (optional): Partial or full city name.
    - `state` (optional): Partial or full state name.
- **Examples**:
    - `GET /api/users/search-by-address-or?city=Springfield&state=NY` (City OR State)
    - `GET /api/users/search-by-address-or?city=Themyscira`
- **Response**: 200 OK with a list of UserResponseDTOs.

#### GET `/api/users/sorted-by-address`
- **Description**: Retrieve all users, sorted by a property of their first address.
- **Query Parameters**:
    - `page` (optional, default 0): Page number.
    - `size` (optional, default 10): Page size.
    - `sort` (optional, default addresses.city,asc): Sort property (addresses.city or addresses.state) and direction (asc or desc).
- **Examples**:
    - `GET /api/users/sorted-by-address` (Sorts by city, ascending)
    - `GET /api/users/sorted-by-address?sort=addresses.state,desc` (Sorts by state, descending)
- **Response**: 200 OK with a list of UserResponseDTOs.

### Separate Address Management (`/api/addresses`)

#### GET `/api/addresses`
- **Description**: Retrieve all addresses in the system, regardless of which user they belong to.
- **Response**: 200 OK with a list of AddressResponseDTO.

#### GET `/api/addresses/{id}`
- **Description**: Retrieve a specific address by its ID.
- **Path Variable**: `{id}`
- **Response**: 200 OK with AddressResponseDTO or 404 Not Found.

##  Testing with Postman

1. Start the Spring Boot application.
2. Open Postman (or your preferred API client).
3. Use the API Endpoints section above to construct your requests.
4. **Headers**: For POST and PUT requests, set `Content-Type: application/json` in the request headers.
5. Start by creating a few users with addresses, then try fetching, updating, deleting, and finally test the search and sort functionalities.
