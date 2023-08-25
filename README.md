# StockM User Service

StockM User Service is a Spring Boot application designed to manage user accounts and authentication for the Stock Management project. This service provides RESTful APIs to create, update, and retrieve user information, as well as handle authentication and authorization.
## Architecture
![Alt text](<chrome-capture-2023-7-25 (3).gif>)

## Features
- User registration
- User authentication (login/logout)
- User profile management (update personal information, change password)
- Role-based authorization (admin, user)

## Prerequisites

To run the StockM User Service, you'll need:

- JDK 11 or later
- Maven 3.6 or later

## Setup

1. Clone the repository:

```bash
git clone https://github.com/urutare/stockm-user-service.git
```

2. Navigate to the project directory:

```bash
cd stockm-user-service
```

3. Configure the `application.properties` file in the `src/main/resources` directory with your database and authentication settings. Make sure to add the necessary database driver dependencies to the `pom.xml` file.

4. Build the application using Maven:

```bash
mvn clean install
```

5. Run the application:

```bash
mvn spring-boot:run
```

The StockM User Service will be up and running at `http://localhost:8080`.

## API Endpoints

Here are some of the main API endpoints:

- `POST /api/users/register`: Register a new user
- `POST /api/users/login`: Authenticate a user (login)
- `POST /api/users/logout`: Logout a user
- `GET /api/users/{id}`: Retrieve user information by ID
- `PUT /api/users/{id}`: Update user information
- `PUT /api/users/{id}/password`: Update a user's password
- `GET /api/users`: List all users (admin only)
- `DELETE /api/users/{id}`: Delete a user (admin only)

For a complete list of API endpoints and usage, please refer to the API documentation.

## Contributing

If you'd like to contribute to the StockM User Service, please follow the standard GitHub workflow:

1. Fork the repository
2. Create a new branch for your changes
3. Commit your changes to your branch
4. Create a pull request to merge your changes into the main repository
5. Address any feedback and resolve conflicts, if necessary

## License

StockM User Service is released under the [MIT License](https://opensource.org/licenses/MIT).

