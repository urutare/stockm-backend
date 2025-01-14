#!/bin/bash
# Array of repository names
# shellcheck disable=SC2054
repositories=("stockm-config-server" "stockm-category-service" "stockm-sync-service" "stockm-stock-service" "stockm-discovery-service" "stockm-auth-service" "stockm-api-gateway" "stockm-payment-service" "stockm-common-core" "sms-gateway" "POSUtils" "POSFunctional")
# Base URL for the GitHub organization
base_url="https://github.com/urutare/"
# Loop through the repositories array
for r in "${repositories[@]}"; do
    # check if the repository exists then skip
    if [ -d "$r" ]; then
        echo "$r already exists"
    else
        git clone "${base_url}${r}.git"
    fi
done
# Call run.sh
# ./run.sh