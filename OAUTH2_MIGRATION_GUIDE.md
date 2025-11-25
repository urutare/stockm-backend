# OAuth2 Migration Guide - StockM Backend

## Overview

This guide documents the migration from shared JWT secrets to Spring Security OAuth2 for the StockM microservices platform.

## Architecture Design

### Before (Current JWT Implementation)

```
Client → API Gateway → Services (validate JWT with shared secret)
         ↓
    Auth Service (generates JWT)
```

### After (OAuth2 Implementation)

```
Client → API Gateway (OAuth2 Client + Token Relay)
         ↓
    Authorization Server (stockm-auth-service)
         ↓
    Resource Servers (category, sync, stock, payment, storage)
         ↓
    Token Introspection Endpoint
```

## Service Roles

### 1. Authorization Server: `stockm-auth-service`

- **Framework**: Spring Authorization Server
- **Responsibilities**:
  - OAuth2 token endpoint (`/oauth2/token`)
  - Token introspection endpoint (`/oauth2/introspect`)
  - Client registration
  - User authentication
  - Token generation (JWT with RS256)
  - JWKS endpoint (`/oauth2/jwks`)

### 2. API Gateway: `stockm-api-gateway`

- **Framework**: Spring Cloud Gateway + OAuth2 Client
- **Responsibilities**:
  - Token relay to downstream services
  - Public endpoint protection
  - OAuth2 login flow (optional for web clients)
  - Route-based authorization

### 3. Resource Servers: `stockm-category-service`, `stockm-sync-service`, `stockm-stock-service`, `stockm-payment-service`, `stockm-storage-service`

- **Framework**: Spring Security OAuth2 Resource Server
- **Responsibilities**:
  - JWT validation using JWKS
  - Method-level security with @PreAuthorize
  - Extract user context from JWT claims

### 4. Discovery Service: `stockm-discovery-service`

- **No Authentication Required**: Internal service registry

## Token Flow

### Password Grant Flow (for existing clients)

```
1. Client → POST /oauth2/token
   Body: {
     grant_type: "password",
     username: "user@example.com",
     password: "password",
     client_id: "stockm-client",
     client_secret: "secret"
   }

2. Authorization Server → Validates credentials → Returns JWT

3. Client → API Gateway with Bearer token

4. Gateway → Relays token to Resource Server

5. Resource Server → Validates JWT using JWKS → Processes request
```

### Client Credentials Flow (for service-to-service)

```
1. Service → POST /oauth2/token
   Body: {
     grant_type: "client_credentials",
     client_id: "service-client",
     client_secret: "secret",
     scope: "service.read service.write"
   }

2. Authorization Server → Returns JWT

3. Service uses JWT for API calls
```

## Key Changes

### Security Benefits

1. **No Shared Secrets**: Asymmetric RS256 instead of HS256
2. **Token Introspection**: Centralized token validation
3. **Standard Protocol**: OAuth2 compliance
4. **Better Scalability**: JWKS-based validation (no network calls)
5. **Refresh Tokens**: Built-in support
6. **Scopes & Authorities**: Fine-grained access control

### Breaking Changes

1. **Endpoint Changes**:

   - `/api/auth/login` → `/oauth2/token`
   - `/api/auth/refresh` → `/oauth2/token` (with refresh_token grant)

2. **Token Format**:

   - Claims structure changes (standard OAuth2 claims)
   - `authorities` → `scope` and `authorities`

3. **Client Configuration**:
   - Must register OAuth2 clients
   - Client credentials required

## Migration Steps

### Phase 1: Authorization Server (stockm-auth-service)

- [ ] Add Spring Authorization Server dependency
- [ ] Configure JWK source (RSA key pair)
- [ ] Register OAuth2 clients
- [ ] Configure token settings
- [ ] Migrate existing endpoints to OAuth2
- [ ] Add token introspection endpoint

### Phase 2: Resource Servers

- [ ] Add OAuth2 Resource Server dependencies
- [ ] Configure JWT validation with JWKS URI
- [ ] Update security configurations
- [ ] Migrate JWT utilities to OAuth2 context
- [ ] Update method security annotations

### Phase 3: API Gateway

- [ ] Add OAuth2 Client dependencies
- [ ] Configure token relay filter
- [ ] Update route configurations
- [ ] Add OAuth2 login support (optional)

### Phase 4: Configuration & Deployment

- [ ] Update environment variables
- [ ] Update Docker compose files
- [ ] Update Kubernetes manifests
- [ ] Update CI/CD pipelines

### Phase 5: Testing & Rollback Plan

- [ ] Integration tests for OAuth2 flows
- [ ] Update Postman/API collections
- [ ] Performance testing
- [ ] Document rollback procedure

## Environment Variables

### Authorization Server (stockm-auth-service)

```bash
# Remove
JWT_SECRET_KEY=xxx

# Add
SPRING_SECURITY_OAUTH2_AUTHORIZATIONSERVER_ISSUER=http://auth-service:8081
OAUTH2_CLIENT_STOCKM_CLIENT_ID=stockm-client
OAUTH2_CLIENT_STOCKM_CLIENT_SECRET={bcrypt}$2a$10$...
OAUTH2_CLIENT_GATEWAY_CLIENT_ID=gateway-client
OAUTH2_CLIENT_GATEWAY_CLIENT_SECRET={bcrypt}$2a$10$...
```

### Resource Servers (category, sync, stock, payment, storage)

```bash
# Remove
JWT_SECRET_KEY=xxx
JWT_ACCESS_TOKEN_EXPIRATION_IN_MS=xxx
JWT_REFRESH_TOKEN_EXPIRATION_IN_MS=xxx

# Add
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://auth-service:8081
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI=http://auth-service:8081/oauth2/jwks
```

### API Gateway

```bash
# Remove
JWT_SECRET_KEY=xxx

# Add
SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_STOCKM_ISSUER_URI=http://auth-service:8081
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_STOCKM_CLIENT_ID=gateway-client
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_STOCKM_CLIENT_SECRET=gateway-secret
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_STOCKM_SCOPE=openid,profile,email
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_STOCKM_AUTHORIZATION_GRANT_TYPE=authorization_code
```

## Testing

### Manual Testing with cURL

```bash
# Get access token
curl -X POST http://localhost:8080/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "username=user@example.com" \
  -d "password=password" \
  -d "client_id=stockm-client" \
  -d "client_secret=secret"

# Use token
curl -X GET http://localhost:8080/api/categories \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"

# Introspect token
curl -X POST http://localhost:8081/oauth2/introspect \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "token=${ACCESS_TOKEN}" \
  -d "client_id=stockm-client" \
  -d "client_secret=secret"
```

## Rollback Plan

If issues arise, rollback by:

1. Revert to previous Docker images
2. Restore JWT environment variables
3. Restart services in order: discovery → auth → gateway → resources
4. Monitor health endpoints

## References

- [Spring Authorization Server](https://spring.io/projects/spring-authorization-server)
- [OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [Spring Cloud Gateway OAuth2](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/gatewayfilter-factories/tokenrelay-factory.html)
