# OAuth2 Migration - Quick Start Guide

## 🎯 Overview

This migration upgrades the StockM Backend from shared JWT secrets to **Spring Security OAuth2** with industry-standard security practices.

## ✅ What's Been Completed

### 1. **Authorization Server** (stockm-auth-service)

- ✅ OAuth2 Authorization Server configured
- ✅ RSA256 JWT signing (instead of HS256 with shared secrets)
- ✅ JWKS endpoint for public key distribution
- ✅ Token introspection endpoint
- ✅ Three OAuth2 clients registered:
  - `stockm-client` (web/mobile apps)
  - `gateway-client` (API Gateway)
  - `service-client` (service-to-service)

### 2. **Resource Server Example** (stockm-category-service)

- ✅ OAuth2 Resource Server configured
- ✅ JWT validation via JWKS
- ✅ Authority extraction from tokens
- ✅ Stateless security

### 3. **Documentation**

- ✅ `OAUTH2_MIGRATION_GUIDE.md` - Complete architecture and migration strategy
- ✅ `OAUTH2_IMPLEMENTATION_SUMMARY.md` - Detailed implementation steps
- ✅ `migrate-oauth2-resource-servers.sh` - Helper script

## 🚀 Next Steps (Priority Order)

### Phase 1: Complete Resource Servers (2-3 hours)

Apply OAuth2 Resource Server pattern to:

- [ ] stockm-stock-service
- [ ] stockm-sync-service
- [ ] stockm-payment-service
- [ ] stockm-storage-service

**How to do it:**

1. Copy OAuth2 dependency from `stockm-category-service/pom.xml`
2. Copy `SecurityConfiguration.java` from `stockm-category-service`
3. Add environment variables (see below)

### Phase 2: Update API Gateway (2 hours)

- [ ] Add OAuth2 Client dependencies
- [ ] Configure Token Relay
- [ ] Update security configuration

**Reference:** See `OAUTH2_IMPLEMENTATION_SUMMARY.md` section 3

### Phase 3: Update Configurations (1 hour)

- [ ] Update all Envs/.env.\* files
- [ ] Update docker-compose.prod.yaml
- [ ] Update docker-compose.dev.yaml
- [ ] Update k8s.yaml

### Phase 4: Testing (2 hours)

- [ ] Test token generation
- [ ] Test token validation
- [ ] Test service-to-service calls
- [ ] Test through API Gateway

### Phase 5: Client Migration (varies)

- [ ] Update frontend/mobile apps to use OAuth2 flows
- [ ] Update API documentation
- [ ] Update Postman collections

## 🔑 Key OAuth2 Endpoints

```bash
# Authorization Server (port 8081)
POST   /oauth2/token          # Get tokens
POST   /oauth2/introspect     # Validate tokens
POST   /oauth2/revoke         # Revoke tokens
GET    /oauth2/jwks           # Public keys
GET    /.well-known/openid-configuration  # Discovery

# All services via Gateway (port 8080)
GET    /api/categories        # Protected resource
GET    /api/stock             # Protected resource
```

## 📋 Environment Variables Cheat Sheet

### Auth Service

```bash
SPRING_SECURITY_OAUTH2_AUTHORIZATIONSERVER_ISSUER=http://auth-service:8081
```

### Resource Services (category, stock, sync, payment, storage)

```bash
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://auth-service:8081
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI=http://auth-service:8081/oauth2/jwks
```

### API Gateway

```bash
SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_GATEWAY_ISSUER_URI=http://auth-service:8081
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GATEWAY_CLIENT_ID=gateway-client
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GATEWAY_CLIENT_SECRET=gateway-client-secret
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://auth-service:8081
```

### Remove from ALL services

```bash
JWT_SECRET_KEY                          # ❌ Remove
JWT_ACCESS_TOKEN_EXPIRATION_IN_MS      # ❌ Remove
JWT_REFRESH_TOKEN_EXPIRATION_IN_MS     # ❌ Remove
```

## 🧪 Quick Test

```bash
# 1. Get access token
curl -X POST http://localhost:8081/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -u "service-client:service-client-secret" \
  -d "grant_type=client_credentials&scope=service.read service.write"

# 2. Extract token from response
TOKEN="<access_token_from_response>"

# 3. Call protected endpoint
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/categories

# 4. Verify token
curl -X POST http://localhost:8081/oauth2/introspect \
  -u "service-client:service-client-secret" \
  -d "token=$TOKEN"
```

## 🏗️ Architecture Diagram

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ 1. Request Token
       ▼
┌─────────────────────┐
│ Authorization Server│ (port 8081)
│  (auth-service)     │
│  - Issues JWT       │
│  - RS256 signing    │
│  - JWKS endpoint    │
└──────┬──────────────┘
       │ 2. Returns JWT
       │
┌──────▼──────┐
│   Client    │
└──────┬──────┘
       │ 3. API Request + JWT
       ▼
┌─────────────────────┐
│   API Gateway       │ (port 8080)
│  - Validates JWT    │
│  - Token Relay      │
└──────┬──────────────┘
       │ 4. Forward Request + JWT
       ▼
┌─────────────────────┐
│ Resource Servers    │
│  - category-service │ (port 8082)
│  - stock-service    │ (port 8084)
│  - sync-service     │ (port 8083)
│  - payment-service  │ (port 8086)
│  - storage-service  │
│                     │
│  - Validate JWT via │
│    JWKS             │
└─────────────────────┘
```

## 🔒 Security Improvements

| Before                    | After                        |
| ------------------------- | ---------------------------- |
| Shared HS256 secret       | RSA256 asymmetric keys       |
| Secret in every service   | Public key validation (JWKS) |
| Custom JWT implementation | Spring OAuth2 standard       |
| No token introspection    | Centralized introspection    |
| Manual token refresh      | Built-in refresh flow        |
| Basic scopes              | Fine-grained authorities     |

## 📚 Documentation Files

1. **OAUTH2_MIGRATION_GUIDE.md** - Architecture, flows, and strategy
2. **OAUTH2_IMPLEMENTATION_SUMMARY.md** - Detailed implementation steps
3. **migrate-oauth2-resource-servers.sh** - Helper script for bulk updates
4. **README_OAUTH2.md** - This file (quick reference)

## ⚠️ Important Notes

### Client Secrets in Production

The default client secrets are for development only. Generate strong secrets for production:

```bash
# Generate secure client secrets
openssl rand -base64 32

# Update in environment variables
STOCKM_CLIENT_SECRET=<generated-secret>
GATEWAY_CLIENT_SECRET=<generated-secret>
SERVICE_CLIENT_SECRET=<generated-secret>
```

### RSA Key Persistence

The current implementation generates RSA keys at startup. For production:

- Store keys in a secure vault (e.g., Azure Key Vault, AWS KMS)
- Or use Java KeyStore (JKS) files
- Ensure keys persist across restarts

### Service Startup Order

Critical order for OAuth2:

1. **auth-service** (must start first)
2. **api-gateway**
3. All other services (can start in parallel)

Update `docker-compose.yaml` dependencies:

```yaml
depends_on:
  auth-service:
    condition: service_healthy
```

## 🆘 Troubleshooting

### "Invalid token signature"

- Check JWKS URL is accessible
- Verify issuer URI matches exactly
- Ensure auth-service is running

### "No suitable HttpMessageConverter found"

- Missing OAuth2 Resource Server dependency
- Check pom.xml has `spring-boot-starter-oauth2-resource-server`

### "Client authentication failed"

- Verify client ID and secret
- Check Basic Auth header encoding
- Confirm client is registered in AuthorizationServerConfig

### "Issuer mismatch"

- Issuer in token must match configured issuer URI
- Check environment variables are consistent
- Verify Docker network resolution (use service names)

## 📞 Support

For questions or issues:

1. Check `OAUTH2_MIGRATION_GUIDE.md` for architecture details
2. Review `OAUTH2_IMPLEMENTATION_SUMMARY.md` for step-by-step instructions
3. Examine working example in `stockm-category-service`
4. Test with provided curl commands above

## 🎉 Benefits

- ✅ Enhanced security with industry standards
- ✅ No shared secrets across services
- ✅ Centralized token management
- ✅ Built-in token refresh
- ✅ Service-to-service authentication
- ✅ Better scalability (JWKS validation)
- ✅ OAuth2/OpenID Connect compliant
- ✅ Ready for production deployment

---

**Last Updated:** November 25, 2025  
**Status:** Authorization Server ✅ | Resource Servers 🔄 | Gateway ⏳ | Testing ⏳
