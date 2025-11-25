#!/bin/bash

# OAuth2 Resource Server Migration Script
# Applies OAuth2 Resource Server configuration to remaining services

set -e

SERVICES=("stockm-stock-service" "stockm-sync-service" "stockm-payment-service" "stockm-storage-service")
BASE_DIR="/Users/diy/Desktop/Witwad/Urutare/ingenzi/v0.0.1/stockm-backendv2"

echo "🔐 Starting OAuth2 Resource Server migration for remaining services..."

for SERVICE in "${SERVICES[@]}"; do
    echo ""
    echo "📦 Processing $SERVICE..."
    
    SERVICE_DIR="$BASE_DIR/$SERVICE"
    
    if [ ! -d "$SERVICE_DIR" ]; then
        echo "⚠️  Warning: $SERVICE_DIR not found, skipping..."
        continue
    fi
    
    # 1. Add OAuth2 Resource Server dependency to pom.xml
    echo "  ✓ Checking pom.xml..."
    POM_FILE="$SERVICE_DIR/pom.xml"
    
    if [ -f "$POM_FILE" ]; then
        if ! grep -q "spring-boot-starter-oauth2-resource-server" "$POM_FILE"; then
            echo "  → Adding OAuth2 Resource Server dependency..."
            # This would need to be done manually or with a more sophisticated script
            echo "    ⚠️  Please add manually:"
            echo "    <dependency>"
            echo "        <groupId>org.springframework.boot</groupId>"
            echo "        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>"
            echo "    </dependency>"
        else
            echo "  ✓ OAuth2 dependency already present"
        fi
    fi
    
    # 2. Create application.yml if it doesn't exist
    echo "  ✓ Checking application.yml..."
    RESOURCES_DIR="$SERVICE_DIR/src/main/resources"
    APP_YML="$RESOURCES_DIR/application.yml"
    
    if [ -f "$APP_YML" ]; then
        if ! grep -q "oauth2" "$APP_YML"; then
            echo "  → application.yml found but needs OAuth2 configuration"
            echo "    Please add:"
            echo "    spring:"
            echo "      security:"
            echo "        oauth2:"
            echo "          resourceserver:"
            echo "            jwt:"
            echo "              issuer-uri: \${SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI:http://auth-service:8081}"
            echo "              jwk-set-uri: \${SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI:http://auth-service:8081/oauth2/jwks}"
        else
            echo "  ✓ OAuth2 configuration already present"
        fi
    else
        echo "  ℹ️  application.yml not found, using environment variables"
    fi
    
    # 3. Find and note SecurityConfiguration.java file
    echo "  ✓ Checking security configuration..."
    SECURITY_CONFIG=$(find "$SERVICE_DIR/src" -name "*Security*.java" 2>/dev/null | head -n 1)
    
    if [ -n "$SECURITY_CONFIG" ]; then
        echo "  → Security configuration found at: $SECURITY_CONFIG"
        echo "    Please update to use OAuth2 Resource Server pattern (see stockm-category-service/SecurityConfiguration.java)"
    else
        echo "  ⚠️  No security configuration found"
    fi
    
    echo "  ✓ $SERVICE processed"
done

echo ""
echo "✅ Migration script completed!"
echo ""
echo "📝 Manual Steps Required:"
echo "  1. Add OAuth2 dependencies to pom.xml files"
echo "  2. Update SecurityConfiguration.java in each service"
echo "  3. Update environment variable files in Envs/ directory"
echo "  4. Update docker-compose.prod.yaml and docker-compose.dev.yaml"
echo "  5. Test each service individually"
echo ""
echo "📖 See OAUTH2_IMPLEMENTATION_SUMMARY.md for detailed instructions"
