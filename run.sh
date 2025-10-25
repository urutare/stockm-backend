
#!/bin/bash

# Exit on error
set -e

# Script path
SCRIPT_PATH="`dirname \"$0\"`"

# Services to build
services=(
"stockm-config-server"
"stockm-category-service"
"stockm-sync-service"
"stockm-stock-service"
"stockm-discovery-service"
"stockm-auth-service"
"stockm-api-gateway"
"stockm-payment-service"
"stockm-storage-service"
)

# Docker-compose command
DCOMPOSE="docker-compose -f ${SCRIPT_PATH}/docker-compose.yaml"
echo "Using docker-compose file: ${SCRIPT_PATH}/docker-compose.yaml"

# Build new Docker image for each service
for service in "${services[@]}"; do
    echo "Building Docker image for ${service}..."
    "${SCRIPT_PATH}/${service}"/mvnw -f "${SCRIPT_PATH}/${service}/pom.xml" jib:dockerBuild
done

# Delete possibly existing container (but retain volumes)
$DCOMPOSE down
echo

# Start docker-compose file
$DCOMPOSE up -d
echo

echo "All services are up and running."
