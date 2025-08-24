# 🏢 Ingenzi Certificate Invoicing System (StockM Backend)

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9.11-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

**Enterprise-grade microservices platform for stock management and point-of-sale operations**

## 🏗️ Architecture

![Architecture Overview](<chrome-capture-2023-7-25 (3).gif>)

The Ingenzi CIS is a comprehensive enterprise solution built on a microservices architecture, providing:
- **Stock Management** - Complete inventory tracking and management
- **Point of Sale (POS)** - Full-featured retail operations system  
- **Multi-tenant** - Support for multiple business entities
- **Real-time synchronization** - Live data updates across all services
- **Payment processing** - Integrated payment solutions
- **SMS notifications** - Customer and business communications

## 🚀 Quick Start

### Prerequisites

- **Java 21** (required) - [Download](https://adoptium.net/temurin/releases/?version=21)
- **Maven 3.9+** - [Download](https://maven.apache.org/download.cgi)
- **Docker** - [Download](https://www.docker.com/get-started)
- **Git** - [Download](https://git-scm.com/downloads)

### 1. Clone and Setup

```bash
# Clone the repository
git clone https://github.com/urutare/stockm-backend.git
cd stockm-backend

# Setup all microservice dependencies (REQUIRED FIRST STEP)
./setup.sh          # Linux/Mac
# OR
setup.bat           # Windows
```

### 2. Environment Configuration

```bash
# Create secrets directory and copy environment template
mkdir -p secrets
cp .env.example secrets/.env

# Edit secrets/.env with your actual configuration values
# See .env.example for required variables
```

### 3. Choose Your Development Approach

#### Option A: Docker Development (Recommended)

```bash
# Start full development environment
make dev
# OR
docker-compose -f docker-compose.prod.yaml up --build

# View logs
make logs

# Stop services
make down
```

#### Option B: Local Maven Development

```bash
# Use Maven wrapper for consistent builds
./mvnw clean compile -DskipTests

# Run tests
./mvnw test

# Package services
./mvnw clean package -DskipTests
```

## 📁 Project Structure

### Core Microservices
```
stockm-backend/
├── stockm-discovery-service/    # Eureka service registry (Port 8761)
├── stockm-config-server/        # Centralized configuration (Port 8888)
├── stockm-api-gateway/          # API gateway and routing (Port 8080)
├── stockm-auth-service/         # Authentication & authorization (Port 8081)
├── stockm-sync-service/         # Data synchronization (Port 8083)
├── stockm-stock-service/        # Inventory management (Port 8084)
├── stockm-category-service/     # Product categories (Port 8082)
├── stockm-payment-service/      # Payment processing (Port 8086)
├── stockm-storage-service/      # File and media storage
├── stockm-common-core/          # Shared libraries and utilities
└── sms-gateway/                 # SMS service (Python, Port 8085)
```

### POS System Modules
```
POS Framework/
├── POSBase/                     # Base POS framework
├── POSCore/                     # Core POS functionality
├── POSDatabase/                 # Database abstraction layer
├── POSUtils/                    # Utility functions
├── POSFunctional/               # Functional components
├── POSUserCore/                 # User management
├── POSSecurityCore/             # Security framework
├── POSStockCore/                # POS inventory management
├── POSPurchaseCore/             # Purchase operations
├── POSSaleCore/                 # Sales operations
├── POSAccountingCore/           # Financial accounting
├── POSMainCore/                 # Main POS application
├── POSDiscoveryCore/            # Service discovery for POS
├── POSIntegrationCore/          # Integration layer
├── POSTranslation/              # Internationalization
└── POSVSDC/                     # Virtual Sales Data Collector
```

## 🛠️ Development Workflow

### Code Quality Standards

The project enforces comprehensive code quality standards:

- **[Spotless](https://github.com/diffplug/spotless)** - Automatic code formatting
- **[Checkstyle](https://checkstyle.sourceforge.io/)** - Java coding standards
- **[PMD](https://pmd.github.io/)** - Source code analyzer
- **[SpotBugs](https://spotbugs.github.io/)** - Bug pattern detection
- **[JaCoCo](https://www.jacoco.org/)** - Code coverage analysis
- **[OWASP](https://owasp.org/www-project-dependency-check/)** - Security vulnerability scanning

### Build Profiles

```bash
# Development (default) - Fast builds, unit tests only
./mvnw clean package

# Production - Full quality checks and tests
./mvnw clean package -Pprod

# Testing - All tests enabled
./mvnw clean package -Ptest

# Docker - Build container images
./mvnw clean package -Pdocker

# Quality - Run all code quality checks
./mvnw clean package -Pquality

# Quick - Skip tests and quality checks
./mvnw clean package -Pquick
```

### Code Formatting

```bash
# Check code formatting
./mvnw spotless:check

# Apply code formatting
./mvnw spotless:apply

# Format specific file types
./mvnw spotless:apply -Dspotless.java.target="src/**/*.java"
```

### Testing

```bash
# Run unit tests
./mvnw test

# Run integration tests  
./mvnw failsafe:integration-test

# Run all tests
./mvnw verify

# Generate test coverage report
./mvnw jacoco:report
```

## 🐳 Docker Operations

### Using Make Commands

```bash
# Start development environment
make dev

# Start in background
make start

# View logs from all services
make logs

# Stop all services
make down

# Hard reset (remove volumes)
make hard-down

# Build all images
make build
```

### Manual Docker Commands

```bash
# Build and start all services
docker-compose -f docker-compose.prod.yaml up --build

# Start specific service
docker-compose -f docker-compose.prod.yaml up stockm-auth-service

# View service logs
docker-compose -f docker-compose.prod.yaml logs -f stockm-api-gateway

# Scale services
docker-compose -f docker-compose.prod.yaml up --scale stockm-stock-service=3
```

## 🔧 Configuration

### Environment Variables

Key configuration variables (set in `secrets/.env`):

```bash
# Database Configuration
POSTGRES_DB=stockm_dev
POSTGRES_USER=stockm_user
POSTGRES_PASSWORD=your_secure_password

# Redis Configuration  
REDIS_PASSWORD=your_redis_password

# JWT Security
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400

# External APIs
SMS_API_KEY=your_sms_api_key
PAYMENT_API_KEY=your_payment_api_key
```

### Service-Specific Configuration

Each microservice can be configured through:
- `application.yml` - Default configuration
- `application-{profile}.yml` - Environment-specific config
- Environment variables - Runtime overrides
- Config server - Centralized configuration

## 📊 Monitoring and Health Checks

### Health Check Endpoints

All services expose actuator endpoints:

```bash
# Service health
curl http://localhost:8080/actuator/health

# Service metrics
curl http://localhost:8080/actuator/metrics

# Service info
curl http://localhost:8080/actuator/info
```

### Service Discovery

Access Eureka dashboard: http://localhost:8761

### API Documentation

Interactive API documentation available at:
- Gateway: http://localhost:8080/swagger-ui.html
- Individual services: http://localhost:{port}/swagger-ui.html

## 🧪 Testing Strategy

### Test Categories

1. **Unit Tests** - Fast, isolated component tests
2. **Integration Tests** - Service integration validation  
3. **Contract Tests** - API contract verification
4. **End-to-End Tests** - Full workflow validation

### Test Execution

```bash
# Fast feedback loop (unit tests only)
./mvnw test -Dtest="*Test"

# Service integration tests
./mvnw test -Dtest="*IntegrationTest"

# Full test suite
./mvnw verify
```

## 🚀 Deployment

### Local Development

```bash
# Start infrastructure only
docker-compose -f docker-compose.dev.yaml up postgres redis kafka

# Run services locally
./mvnw spring-boot:run -pl stockm-discovery-service
./mvnw spring-boot:run -pl stockm-config-server  
./mvnw spring-boot:run -pl stockm-api-gateway
```

### Production Deployment

```bash
# Build production images
./mvnw clean package -Pprod,docker

# Deploy with Docker Compose
docker-compose -f docker-compose.prod.yaml up -d

# Deploy with Kubernetes
kubectl apply -f k8s.yaml
```

## 📚 Documentation

- **[Setup Guide](VSCODE-SETUP.md)** - VS Code development environment
- **[API Documentation](http://localhost:8080/swagger-ui.html)** - Interactive API docs
- **[Architecture Guide](Writerside/)** - Detailed system architecture
- **[Deployment Guide](docker-compose.prod.yaml)** - Production deployment

## 🤝 Contributing

### Development Setup

1. Follow the Quick Start guide above
2. Configure your IDE with the provided settings:
   - VS Code: Use `ingenzi-cis.code-workspace`
   - IntelliJ: Import as Maven project
3. Run code quality checks before committing:
   ```bash
   ./mvnw spotless:apply
   ./mvnw verify -Pquality
   ```

### Code Standards

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Write comprehensive JavaDoc for public APIs
- Maintain minimum 80% test coverage
- All commits must pass quality gates

### Pull Request Process

1. Create feature branch from `develop`
2. Implement changes with tests
3. Run quality checks: `./mvnw verify -Pquality`
4. Submit PR with clear description
5. Address review feedback
6. Merge after approval

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Issues**: [GitHub Issues](https://github.com/urutare/stockm-backend/issues)
- **Documentation**: [Project Wiki](https://github.com/urutare/stockm-backend/wiki)
- **Community**: [GitHub Discussions](https://github.com/urutare/stockm-backend/discussions)

---

**Built with ❤️ by the Urutare Team**

