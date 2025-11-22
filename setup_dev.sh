#!/bin/bash
# Array of repository names
repositories=(
"stockm-config-server"
"stockm-category-service"
"stockm-sync-service"
"stockm-stock-service"
"stockm-discovery-service"
"stockm-auth-service"
"stockm-api-gateway"
"stockm-payment-service"
"stockm-common-core"
"stockm-storage-service"
"sms-gateway"
"POSUtils"
"POSFunctional"
"POSVSDC"
"POSStockCore"
"POSPurchaseCore"
"POSSaleCore"
"POSUserCore"
"POSAccountingCore"
"POSMainCore"
"POSDiscoveryCore"
"POSSecurityCore"
"POSTranslation"
"POSCore"
"POSDatabase"
"POSBase"
"POSIntegrationCore"
"POSExportBase"
"POSUserExport"
"POSStockExport"
"POSPurchaseExport"
"POSSaleExport"
"POSAccountingExport"
"POSMainExport"
)


# Base URL for the GitHub organization
base_url="https://github.com/urutare/"
# Loop through the repositories array
for r in "${repositories[@]}"; do
    # check if the repository exists then pull the latest changes
    if [ -d "$r" ]; then
        echo "Updating $r..."
        cd "$r" || exit
        git checkout develop
        git stash
        git pull origin develop
        cd ..
    else
        echo "$r does not exist. Cloning..."
        git clone "${base_url}${r}.git"
    fi
done