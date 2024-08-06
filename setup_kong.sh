#!/bin/bash
set -e

# Function to wait for Kong to be ready
wait_for_kong() {
  echo "Waiting for Kong Admin API to be ready..."
  until $(curl --output /dev/null --silent --head --fail http://kong:8001); do
    printf '.'
    sleep 5
  done
  echo "Kong is ready!"
}

# Function to add a service
add_service() {
  echo "Adding service 'api-gateway'..."
  curl -i -X POST --url http://kong:8001/services/ \
    --data 'name=api-gateway' \
    --data 'url=http://api-gateway:8080' || {
    echo "Failed to add service 'api-gateway'"
    exit 1
  }
  echo "Service 'api-gateway' added successfully!"
}

# Function to add a route
add_route() {
  echo "Adding route for 'api-gateway' service..."
  curl -i -X POST --url http://kong:8001/services/api-gateway/routes \
    --data 'paths[]=/web-service' || {
    echo "Failed to add route for 'api-gateway' service"
    exit 1
  }
  echo "Route for 'api-gateway' service added successfully!"
}

# Main execution
wait_for_kong
add_service
add_route

echo "Kong configuration completed successfully."
