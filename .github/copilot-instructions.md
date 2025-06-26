# Ingenzi CIS - Development Context

You are working on **Ingenzi**, a Certificate Invoicing System (CIS) built with microservices architecture. This is a large-scale Java/Spring Boot project with Maven multi-module structure.

## Project Architecture

- **Microservices**: Each service is independently deployable
- **Java 21** with **Spring Boot 3.3.4**
- **Maven multi-module** workspace
- **Docker containerization** for all services
- **Eureka** for service discovery
- **Spring Cloud Gateway** as API gateway
- **JWT authentication** with role-based access control

## Service Categories

### Core Services (stockm-\*)

- `stockm-config-server` - Configuration management
- `stockm-discovery-service` - Eureka service registry
- `stockm-api-gateway` - API routing and authentication
- `stockm-auth-service` - Authentication/authorization
- `stockm-category-service` - Product categories
- `stockm-stock-service` - Inventory management
- `stockm-payment-service` - Payment processing
- `stockm-sync-service` - Data synchronization
- `stockm-storage-service` - File storage
- `stockm-common-core` - Shared utilities

### POS Services (POS\*)

Legacy point-of-sale modules integrated into the system:

- `POSCore`, `POSBase`, `POSDatabase` - Core POS functionality
- `POSStockCore`, `POSPurchaseCore`, `POSSaleCore` - Business domains
- `POSUserCore`, `POSSecurityCore` - User management and security
- `POSAccountingCore`, `POSMainCore` - Accounting and main logic
- `POSVSDC` - Fiscal device integration

### Additional Services

- `sms-gateway` - Python/FastAPI SMS notifications

## Code Standards

### Package Structure

```
com.urutare.{service-name}
├── controller/     # REST endpoints
├── service/        # Business logic
├── repository/     # Data access
├── model/          # JPA entities
├── dto/            # Data transfer objects
├── config/         # Configuration classes
└── exception/      # Custom exceptions
```

### Naming Conventions

- **Services**: `stockm-{domain}-service` or `POS{Component}`
- **Classes**: PascalCase (e.g., `InvoiceService`)
- **Methods/Variables**: camelCase (e.g., `calculateTotal`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_ATTEMPTS`)
- **REST endpoints**: kebab-case (e.g., `/api/v1/customer-invoices`)

## Development Guidelines

### When suggesting code:

1. **Follow layered architecture**: Controller → Service → Repository
2. **Use Spring Boot annotations** appropriately (@RestController, @Service, @Repository)
3. **Implement proper error handling** with custom exceptions
4. **Add validation** using Bean Validation (@Valid, @NotNull, etc.)
5. **Include logging** with SLF4J
6. **Write testable code** with dependency injection
7. **Follow RESTful principles** for API design
8. **Use DTOs** for API requests/responses, not entities directly

### For database operations:

- Use **JPA/Hibernate** with proper entity relationships
- Implement **pagination** for list endpoints
- Use **@Transactional** for service methods
- Follow **database naming conventions** (snake_case)

### For microservice communication:

- Use **RestTemplate** or **WebClient** for HTTP calls
- Implement **circuit breakers** for resilience
- Add **service discovery** using Eureka client
- Handle **service failures** gracefully

### Security considerations:

- Always validate input parameters
- Use **JWT tokens** for authentication
- Implement **role-based authorization** (@PreAuthorize)
- Secure sensitive endpoints

## Business Domain Knowledge

This system manages:

- **Invoices** with tax calculations and fiscal compliance
- **Stock/Inventory** with real-time tracking
- **Customers and Suppliers** with relationship management
- **Products and Categories** with hierarchical organization
- **Payments** with multiple payment methods
- **Users and Permissions** with role-based access
- **Reports** for business analytics
- **VSDC integration** for fiscal compliance

## Response Style

When providing code suggestions:

- Explain the architectural reasoning
- Show how the code fits into the microservice structure
- Include relevant Spring Boot best practices
- Suggest appropriate testing approaches
- Consider inter-service communication patterns
- Address security and validation requirements

## Manual Test

- Focus on the actual implementation and ignore the VS Code import errors, then compile to test.
