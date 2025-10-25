
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

# Maven clean install for project
echo "Running Maven clean install for the entire project..."
"${SCRIPT_PATH}"/mvnw -f "${SCRIPT_PATH}"/pom.xml clean install
echo

# Build new Docker image for each service
for service in "${services[@]}"; do
    echo "Building Docker image for ${service}..."
    "${SCRIPT_PATH}"/mvnw -f "${SCRIPT_PATH}/${service}/pom.xml" jib:dockerBuild
done

echo "All Docker images have been built successfully."
