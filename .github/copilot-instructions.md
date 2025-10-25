# Copilot Instructions for Ingenzi CIS (StockM Backend)

## Repository Overview

This is the **Ingenzi Certificate Invoicing System (CIS)**, also known as **StockM Backend** - a complex enterprise-grade microservices platform for stock management and point-of-sale operations. The system serves as the backend for a comprehensive business management solution.

**Repository Type**: Meta-repository with multi-module Maven project  
**Languages**: Java 21 (microservices), Python (SMS gateway)  
**Framework**: Spring Boot 3.3.4 with Spring Cloud microservices  
**Architecture**: Microservices with service discovery, API gateway, and event-driven messaging  
**Size**: 46 Maven modules across 30+ separate repositories  

## Critical Setup Requirements ⚠️

**IMPORTANT**: This repository requires a mandatory setup process before any build operations. The local pom.xml references 46 modules that do not exist until dependencies are cloned.

### 1. Repository Setup (ALWAYS REQUIRED FIRST)

```bash
# Clone all required microservice repositories
./setup.sh          # Linux/Macs
# This clones 30+ repositories including:
# - stockm-* services (core microservices)  
# - POS* modules (Point of Sale system)
# - sms-gateway (Python service)
```

**Note**: Setup scripts require GitHub access and may prompt for credentials. If authentication fails, individual modules can be cloned manually from the urutare organization.

### 2. Environment Configuration

```bash
# Create secrets directory and environment files
mkdir -p secrets
cp .env.example secrets/.env
# SMS Gateway also requires separate environment file
touch secrets/.env.sms  # Create empty file or copy SMS-specific variables
# Edit secrets/.env with actual values (see .env.example for required variables)
```

### 3. Java Version Requirement

- **Required**: Java 21 (specified in pom.xml)
- **Current Environment**: Java 17 (incompatible)
- **Setup Scripts Available**: 
  - `setup-java.bat` / `setup-java.ps1` (Windows)
  - Manual installation required for Linux environments

## Build and Validation Instructions

### Prerequisites Validation
```bash
# Check versions (requirements in parentheses)
java -version        # (Java 21 required)
mvn -version        # (Maven 3.6+)
docker --version    # (Docker for containerized deployment)
make --version      # (GNU Make for Docker operations)
```

### Development Workflow

#### Option 1: Local Development (Maven-based)
**ONLY after setup.sh completes successfully:**

```bash
# 1. Clean and compile all modules
mvn clean compile -DskipTests
# Expected time: 5-10 minutes for first build

# 2. Run tests
mvn test
# Expected time: 10-15 minutes

# 3. Package all services
mvn clean package -DskipTests
# Expected time: 8-12 minutes
```

#### Option 2: Containerized Development (Recommended)
```bash
# 1. Start development environment
make dev
# OR: docker compose -f docker-compose.prod.yaml up --build --remove-orphans
# Expected time: 10-15 minutes for first build

# 2. Start in background
make start
# OR: make up

# 3. View logs
make logs

# 4. Stop and cleanup
make down
```

### Common Build Issues and Solutions

1. **Module Not Found Errors**: Ensure `./setup.sh` completed successfully
2. **Java Version Errors**: Upgrade to Java 21 using provided setup scripts
3. **Environment File Missing**: Create `secrets/.env` from `.env.example`
4. **Docker Permission Errors**: Add user to docker group or run with sudo
5. **Port Conflicts**: Check ports 5432, 8080, 8761, 8888 are available

### Testing Strategy

```bash
# Run unit tests only
mvn test -DfailIfNoTests=false

# Run specific module tests (after setup.sh)
mvn test -pl stockm-auth-service

# Integration tests (requires running infrastructure)
make dev  # Start services first
# Then run integration test suites

# VS Code tasks (Ctrl+Shift+P → Tasks: Run Task)
# - Maven: Clean Install All
# - Maven: Clean Compile  
# - Maven: Test All
# - Docker: Build All Services
```

## Project Architecture and Layout

### Core Microservices (stockm-*)
- **stockm-discovery-service** (Port 8761): Eureka service registry
- **stockm-config-server** (Port 8888): Centralized configuration
- **stockm-api-gateway** (Port 8080): API gateway and routing
- **stockm-auth-service** (Port 8081): User authentication and authorization
- **stockm-category-service** (Port 8082): Product category management
- **stockm-stock-service** (Port 8084): Inventory management
- **stockm-sync-service** (Port 8083): Data synchronization
- **stockm-payment-service** (Port 8086): Payment processing
- **stockm-storage-service**: File and media storage

### POS System Modules (POS*)
Point of Sale system components:
- **POSCore, POSBase**: Core POS framework
- **POSDatabase**: Database abstraction layer
- **POSUserCore, POSSecurityCore**: User management and security
- **POSSaleCore, POSPurchaseCore**: Sales and purchase operations
- **POSStockCore**: Inventory for POS system
- **POSAccountingCore**: Financial accounting
- **POSMainCore**: Main POS application logic

### Supporting Services
- **sms-gateway** (Port 8085): Python-based SMS service
- **stockm-common-core**: Shared libraries and utilities

### Infrastructure Configuration
- **docker-compose.prod.yaml**: Production Docker configuration
- **docker-compose.dev.yaml**: Development environment
- **Makefile**: Docker operation shortcuts
- **k8s.yaml**: Kubernetes deployment configuration
- **.vscode/**: VS Code workspace configuration
- **Envs/**: Service-specific environment templates

### Key Configuration Files
- **pom.xml**: Root Maven configuration with all 46 modules
- **secrets/.env**: Environment variables (create from .env.example)
- **.env.properties.example**: Property-based configuration template
- **prometheus.yml**: Monitoring configuration
- **nginx.conf**: Load balancer configuration

### Development Tools
- **vscode-setup.ps1/bat**: VS Code environment setup
- **setup.sh/bat**: Repository and dependency setup
- **setup-java.ps1/bat**: Java 21 installation scripts
- **.vscode/tasks.json**: Pre-configured VS Code tasks for Maven and Docker operations
- **db-init/**: Database initialization scripts (creates dev/prod databases)

## Validation and CI Information

### Pre-commit Validation
```bash
# Build verification
mvn clean verify -DskipTests

# Dependency security check
mvn dependency:check  # Check for vulnerable dependencies

# Docker compose validation
docker compose -f docker-compose.prod.yaml config  # Validate compose file

# VS Code integration
# Use Ctrl+Shift+P → "Java: Rebuild Projects" after changes
```

### Docker Health Checks
All services include health check endpoints:
- Discovery Server: `http://localhost:8761/actuator/health`
- Config Server: `http://localhost:8888/actuator/health`
- API Gateway: `http://localhost:8080/actuator/health`

### Service Dependencies
**Critical startup order:**
1. PostgreSQL, Redis, Kafka infrastructure
2. Discovery Service (8761)
3. Config Server (8888) 
4. API Gateway (8080)
5. All other microservices

**Always verify infrastructure services are healthy before starting application services.**

## Agent Best Practices

1. **Always run `./setup.sh` first** - The repository cannot build without cloned dependencies
2. **Use containerized development** - More reliable than local Maven builds
3. **Check service health endpoints** - Verify services are properly started
4. **Follow the startup order** - Infrastructure → Discovery → Config → Gateway → Services
5. **Use the Makefile** - Provides tested Docker operation commands
6. **Monitor resource usage** - Large system requiring adequate memory (6GB+ recommended)
7. **Verify Java 21** - Many issues stem from incorrect Java version

## Quick Command Reference

```bash
# Essential commands (in order)
./setup.sh                    # Setup dependencies (REQUIRED FIRST)
mkdir -p secrets && cp .env.example secrets/.env
make dev                      # Start development environment  
make logs                     # Monitor all services
make down                     # Stop and cleanup

# Development commands
mvn clean compile -DskipTests # Build after setup
mvn test                      # Run tests
mvn clean package            # Create deployment artifacts

# Troubleshooting
docker system prune --volumes --force  # Clean Docker state
make hard-down               # Force stop with volume cleanup
```

Trust these instructions and avoid unnecessary exploration unless information is incomplete or incorrect.