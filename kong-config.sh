#!/bin/sh

# Install wget if not already installed
if ! command -v wget &> /dev/null
then
    apt-get autoremove
    apt-get update
    apt-get install -y wget curl
fi

# Wait for the Kong database to be fully ready
while ! wget -q --spider http://kong-database:5432; do
    sleep 3
done

# Wait for Kong to be ready
while ! wget -q --spider http://localhost:8001/status; do
    sleep 3
done

# Configure the API Gateway Service using wget
wget --quiet \
     --method POST \
     --header 'Content-Type: application/x-www-form-urlencoded' \
     --body-data 'name=api-gateway&url=http://api-gateway:8080' \
     --output-document - \
     http://localhost:8001/services/

wget --quiet \
     --method POST \
     --header 'Content-Type: application/x-www-form-urlencoded' \
     --body-data 'paths[]=/api' \
     --output-document - \
     http://localhost:8001/services/api-gateway/routes