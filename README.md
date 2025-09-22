# StockM Backend - Multi-Microservice Architecture

[![Production Deployment](https://github.com/urutare/stockm-backend/actions/workflows/deploy-production.yml/badge.svg?branch=main)](https://github.com/urutare/stockm-backend/actions/workflows/deploy-production.yml)
[![Development Deployment](https://github.com/urutare/stockm-backend/actions/workflows/deploy-development.yml/badge.svg?branch=develop)](https://github.com/urutare/stockm-backend/actions/workflows/deploy-development.yml)
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://adoptium.net/temurin/releases/?version=21)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-green.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

The **Ingenzi Certificate Invoicing System (StockM Backend)** is a comprehensive enterprise-grade microservices platform for stock management and point-of-sale operations. This repository serves as the parent orchestrator for 27+ individual service repositories, providing automated deployment and centralized configuration management.
## Architecture
![Alt text](<chrome-capture-2023-7-25 (3).gif>)
### Requirements

Install the following dependencies globaly on your system:

- Install [Docker](https://www.docker.com/) and [docker-compose](https://www.docker.com/)
- Install `Make` depending on your OS. [Windows Installation](https://linuxhint.com/install-use-make-windows/), [Ubuntu Installation](https://linuxhint.com/install-make-ubuntu/)
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

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for a detailed list of changes, new features, and version history.

## 🚀 Automated Deployment

The StockM Backend includes automated GitHub Actions workflows for seamless deployment to DigitalOcean:

### Production Deployment
- **Trigger**: Push to `main` branch
- **Environment**: Production DigitalOcean droplet
- **Services**: Full microservices stack (27+ services)
- **Features**: Complete health checks, post-deployment verification

### Development Deployment  
- **Trigger**: Push to `develop` branch
- **Environment**: Development DigitalOcean droplet
- **Services**: Core services only for faster deployment
- **Features**: Rapid iteration, pull request validation

### Deployment Status
- 🟢 **Production**: Last deployment status shown in badge above
- 🟡 **Development**: Continuous deployment for testing
- 📊 **Monitoring**: Health endpoints at `:8080/actuator/health`

### Quick Deployment Commands
```bash
# Deploy to production
git push origin main

# Deploy to development
git push origin develop

# Manual deployment
# Go to Actions → Select workflow → Run workflow
```

For detailed deployment setup instructions, see [DEPLOYMENT-SETUP.md](DEPLOYMENT-SETUP.md).

## Contributing

If you'd like to contribute to the StockM Backend, please follow the standard GitHub workflow:

1. Fork the repository
2. Create a new branch for your changes
3. Commit your changes to your branch
4. Create a pull request to merge your changes into the main repository
5. Address any feedback and resolve conflicts, if necessary

### Development Workflow
1. Push changes to `develop` branch for automatic testing
2. Verify deployment in development environment
3. Create PR to `main` for production deployment

## License

StockM Backend is released under the [MIT License](https://opensource.org/licenses/MIT).

