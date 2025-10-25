
#!/bin/bash

# Exit on error
set -e

# Script path
SCRIPT_PATH="`dirname \"$0\"`"

# Docker-compose command
DCOMPOSE="docker-compose -f ${SCRIPT_PATH}/docker-compose.dev.yaml"
echo "Using docker-compose file: ${SCRIPT_PATH}/docker-compose.dev.yaml"

# Delete possibly existing container (but retain volumes)
$DCOMPOSE down
echo

# Start docker-compose file
$DCOMPOSE up -d
echo

echo "All services are up and running."
