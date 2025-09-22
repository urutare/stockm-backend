# Changelog

All notable changes to the Ingenzi Certificate Invoicing System (StockM Backend) will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Changelog file to track project changes and releases

## [0.1.0] - 2024-09-22

### Added
- **Microservices Architecture**: Complete Spring Boot 3.3.4 based microservices system
- **Service Discovery**: Eureka service registry running on port 8761
- **Configuration Management**: Centralized Spring Cloud Config Server on port 8888
- **API Gateway**: Spring Cloud Gateway for routing and load balancing on port 8080
- **Authentication Service**: JWT-based user authentication and authorization service on port 8081
- **Stock Management Service**: Comprehensive inventory management system on port 8084
- **Category Service**: Product category management service on port 8082
- **Sync Service**: Data synchronization service on port 8083
- **Payment Service**: Payment processing and transaction management on port 8086
- **Storage Service**: File and media storage management service
- **SMS Gateway**: Python-based SMS notification service on port 8085

#### Point of Sale (POS) System
- **POSCore**: Core POS framework and base functionality
- **POSBase**: Foundational POS components
- **POSDatabase**: Database abstraction layer for POS operations
- **POSUserCore**: User management for POS system
- **POSSecurityCore**: Security framework for POS operations
- **POSSaleCore**: Sales transaction processing
- **POSPurchaseCore**: Purchase order management
- **POSStockCore**: Inventory management for POS
- **POSAccountingCore**: Financial accounting and reporting
- **POSMainCore**: Main POS application logic
- **POSDiscoveryCore**: Service discovery for POS modules
- **POSTranslation**: Multi-language support for POS
- **POSIntegrationCore**: Third-party integration capabilities
- **POSUtils**: Utility functions and common tools
- **POSFunctional**: Functional programming utilities
- **POSVSDC**: VAT-specific functionality

#### Infrastructure & DevOps
- **Docker Support**: Complete containerization with docker-compose configurations
- **Development Environment**: Ready-to-use development setup with `make dev`
- **Production Environment**: Production-ready Docker Compose configuration
- **Kubernetes Support**: K8s deployment manifests
- **Database**: PostgreSQL database with initialization scripts
- **Monitoring**: Prometheus configuration for metrics collection
- **Load Balancer**: Nginx configuration for load balancing
- **Message Queue**: Kafka integration for event-driven architecture
- **Caching**: Redis integration for distributed caching

#### Development Tools
- **VS Code Integration**: Complete workspace configuration with debugging support
- **Maven Build System**: Multi-module Maven project with 46 modules
- **Java 21 Support**: Latest LTS Java version with optimized configurations
- **Setup Scripts**: Automated setup for dependencies and environment
- **Make Commands**: Simplified Docker operations through Makefile

#### Documentation
- **README**: Comprehensive setup and usage documentation
- **VS Code Setup Guide**: Detailed IDE configuration instructions
- **Writerside Documentation**: Technical documentation framework

### Technical Specifications
- **Java Version**: Java 21 (LTS)
- **Spring Boot**: 3.3.4
- **Spring Cloud**: Latest compatible version
- **Database**: PostgreSQL
- **Cache**: Redis
- **Message Broker**: Apache Kafka
- **Container Runtime**: Docker & Docker Compose
- **Build Tool**: Apache Maven 3.6+
- **Package Manager**: Maven
- **Documentation**: Markdown + Writerside

### Infrastructure Components
- **Service Ports**:
  - Discovery Server: 8761
  - Config Server: 8888
  - API Gateway: 8080
  - Auth Service: 8081
  - Category Service: 8082
  - Sync Service: 8083
  - Stock Service: 8084
  - SMS Gateway: 8085
  - Payment Service: 8086

### Security Features
- JWT-based authentication
- Role-based authorization (Admin, User)
- Secure configuration management
- Environment-based secrets management
- HTTPS support configuration

### Quality Assurance
- Health check endpoints for all services
- Actuator endpoints for monitoring
- Service dependency validation
- Automated testing framework setup
- Code quality configurations

---

### Notes
- This release represents the initial stable version of the StockM Backend system
- All core microservices are operational and ready for production deployment
- The system supports both development and production environments
- Complete documentation and setup guides are included

### Migration Notes
- First stable release - no migration needed
- Environment configuration required (see `.env.example`)
- Run `./setup.sh` to clone all required service repositories
- Java 21 is required for compilation and runtime

### Known Issues
- Setup scripts require GitHub access for repository cloning
- Large memory footprint (6GB+ recommended for full stack)
- Initial startup time can be 10-15 minutes for full environment

### Support
- See README.md for setup instructions
- See VSCODE-SETUP.md for development environment configuration
- Use Docker Compose for the recommended deployment method

[Unreleased]: https://github.com/urutare/stockm-backend/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/urutare/stockm-backend/releases/tag/v0.1.0