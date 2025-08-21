# Copilot Instructions for StockM Backend

## Repository Overview

**StockM Backend** is a comprehensive microservices-based Stock Management and Point of Sale (POS) system built with Spring Boot 3.3.4 and Java 21. This is a **meta-repository** that orchestrates 30+ separate microservice repositories into a cohesive system.

### Key Facts
- **Project Type**: Multi-module Maven project with microservices architecture
- **Primary Languages**: Java 21, Python (SMS Gateway)
- **Frameworks**: Spring Boot 3.3.4, Spring Cloud, React (Frontend components)
- **Deployment**: Docker-first with comprehensive Docker Compose orchestration
- **Database**: PostgreSQL with Redis caching
- **Message Broker**: Apache Kafka with Zookeeper
- **Service Discovery**: Eureka Server
- **API Gateway**: Spring Cloud Gateway
- **Total Services**: 30+ microservices (stockm-* services + POS* components)

## Critical Setup Requirements

### ⚠️ CRITICAL: This repository requires setup before any operations can work

**Before any Maven or build commands**, you MUST run the setup process:

```bash
# For Linux/Mac
./setup.sh

# For Windows
setup.bat
```

This clones 30+ required repositories into the current directory. **All Maven commands will fail until this is done.**

### Java Version Requirement
- **Required**: Java 21 (specified in pom.xml)
- **Common Issue**: Many environments have Java 17 by default
- **Solution**: Run the provided setup scripts:
  ```bash
  # PowerShell (as Administrator)
  .\setup-java.ps1
  
  # Command Prompt (as Administrator)  
  setup-java.bat
  ```

## Build and Validation Instructions

### Environment Setup (ALWAYS REQUIRED)

1. **Create required environment files** (Docker Compose will fail without these):
   ```bash
   mkdir -p secrets
   touch secrets/.env
   touch secrets/.env.sms
   cp .env.example secrets/.env
   ```

2. **Install dependencies globally**:
   - Docker & Docker Compose
   - Java 21 (not 17!)
   - Maven 3.6+
   - Make (for shortcuts)

### Build Process (Sequential Order Required)

**ALWAYS follow this exact sequence:**

1. **Setup repositories** (only needed once):
   ```bash
   ./setup.sh  # This takes 5-10 minutes
   ```

2. **Verify Java version**:
   ```bash
   java -version  # Must show Java 21, not 17
   ```

3. **Maven operations** (after setup only):
   ```bash
   mvn clean compile -DskipTests  # 3-5 minutes
   mvn clean install -DskipTests  # 5-10 minutes
   mvn test  # 10-15 minutes (runs tests across all modules)
   ```

4. **Docker operations**:
   ```bash
   # Quick development start
   make dev  # docker compose up --build
   
   # Production deployment
   make start  # docker compose up -d --build
   
   # Check status
   make ps
   
   # View logs
   make logs
   
   # Clean shutdown
   make down
   ```

### Common Build Issues & Workarounds

**Issue**: `Child module X does not exist`
- **Cause**: Repositories not cloned
- **Solution**: Run `./setup.sh` first

**Issue**: `docker-compose: env file not found`
- **Cause**: Missing secrets/.env files
- **Solution**: `mkdir -p secrets && touch secrets/.env secrets/.env.sms`

**Issue**: Java compilation errors
- **Cause**: Using Java 17 instead of Java 21
- **Solution**: Install Java 21 using provided scripts

**Issue**: Docker build failures
- **Cause**: Modules not available or env files missing
- **Solution**: Ensure setup.sh completed successfully and env files exist

### VS Code Development Setup

The repository includes comprehensive VS Code configuration:

1. **Use the setup script**:
   ```bash
   .\vscode-setup.ps1  # PowerShell
   vscode-setup.bat    # Command Prompt
   ```

2. **Key VS Code tasks available**:
   - `Maven: Clean Install All`
   - `Maven: Clean Compile`
   - `Maven: Test All`
   - `Docker: Build All Services`
   - `Docker: Start All Services`

3. **Extensions auto-configured**:
   - Java Extension Pack
   - Spring Boot Extensions
   - Docker Extensions

## Project Layout & Architecture

### Repository Structure
```
stockm-backend/                 # Meta-repository (this repo)
├── pom.xml                    # Parent POM with 26 modules
├── docker-compose.prod.yaml   # Production orchestration
├── docker-compose.dev.yaml    # Development setup
├── Makefile                   # Docker shortcuts
├── setup.sh/.bat             # Repository clone scripts
├── .vscode/                   # Pre-configured VS Code settings
├── secrets/                   # Environment configuration (create manually)
└── [30+ cloned repositories] # After running setup.sh
```

### Service Categories

**Core StockM Services** (microservices):
- `stockm-discovery-service` - Eureka service registry (port 8761)
- `stockm-config-server` - Centralized configuration (port 8888)
- `stockm-api-gateway` - Main API gateway (port 8080)
- `stockm-auth-service` - Authentication & authorization
- `stockm-user-service` - User management
- `stockm-stock-service` - Inventory management
- `stockm-category-service` - Product categorization
- `stockm-payment-service` - Payment processing
- `stockm-sync-service` - Data synchronization
- `stockm-storage-service` - File storage

**POS Components** (Point of Sale modules):
- `POSCore` - Core POS functionality
- `POSMainCore` - Main POS application
- `POSUserCore` - User management for POS
- `POSSaleCore` - Sales processing
- `POSStockCore` - Inventory for POS
- `POSAccountingCore` - Financial accounting
- `POSSecurityCore` - POS security
- `POSDatabase` - Database layer
- And 8+ additional POS modules

**Support Services**:
- `sms-gateway` - Python-based SMS service
- `postgres-db` - PostgreSQL database
- `redis_cache` - Redis caching
- `kafka-broker` - Message broker
- `zookeeper-server` - Kafka coordination

### Key Configuration Files

**Docker Orchestration**:
- `docker-compose.prod.yaml` - Production with all services
- `docker-compose.dev.yaml` - Development configuration
- `nginx.conf` - Load balancer configuration
- `prometheus.yml` - Monitoring configuration

**Build Configuration**:
- `pom.xml` - Parent Maven configuration
- `Makefile` - Docker operation shortcuts
- `.vscode/tasks.json` - Pre-configured build tasks
- `.vscode/settings.json` - Java/Maven IDE settings

**Environment Configuration**:
- `.env.example` - Template for environment variables
- `secrets/.env` - Production environment (must create)
- `secrets/.env.sms` - SMS service configuration (must create)

### Development Workflow

**For new features/changes**:
1. Ensure setup.sh has been run
2. Make changes in relevant service repositories
3. Test using Docker Compose: `make dev`
4. Verify services at: http://localhost:8080 (API Gateway)
5. Monitor with: `make logs`

**For debugging**:
1. Use VS Code with pre-configured launch configurations
2. Access individual services via Eureka: http://localhost:8761
3. Check service health via actuator endpoints

### Validation Steps

**Pre-commit validation**:
```bash
# 1. Compile all modules
mvn clean compile -DskipTests

# 2. Run tests
mvn test

# 3. Build Docker images
make build

# 4. Test startup
make dev && sleep 30 && make ps
```

**Service health checks**:
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080/actuator/health
- Individual services: http://localhost:{port}/actuator/health

### Important Notes for Agents

1. **Never run Maven commands before setup.sh** - they will always fail
2. **Always use Java 21** - Java 17 will cause compilation errors
3. **Create secrets/ directory** manually - Docker Compose depends on it
4. **Use Make commands** for Docker operations - they handle dependencies correctly
5. **Check .gitignore** - it excludes all cloned repositories (stockm-*, POS*, sms-gateway)
6. **Trust these instructions** - only search/explore if information is incomplete or incorrect

### Time Expectations
- Initial setup (setup.sh): 5-10 minutes
- Full Maven build: 5-10 minutes
- Docker build: 10-15 minutes
- Full test suite: 10-15 minutes
- Service startup: 2-3 minutes

This architecture supports a complete enterprise-grade stock management and POS system with proper microservice separation, service discovery, API gateway, and comprehensive monitoring.