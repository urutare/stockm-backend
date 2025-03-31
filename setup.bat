
@echo off


REM Array of repository names
set repositories=(
"stockm-config-server",
"stockm-category-service",
"stockm-sync-service",
"stockm-stock-service",
"stockm-discovery-service",
"stockm-auth-service",
"stockm-api-gateway",
"stockm-payment-service",
"stockm-common-core",
"sms-gateway",
"POSUtils",
"POSFunctional",
"POSVSDC",
"POSStockCore",
"POSPurchaseCore",
"POSSaleCore",
"POSUserCore",
"POSAccountingCore",
"POSMainCore",
"POSDiscoveryCore",
"POSSecurityCore",
"POSTranslation",
"POSCore",
"POSDatabase",
"POSBase",
"POSIntegrationCore"
)

REM Base URL for the GitHub organization
set base_url=https://github.com/urutare/

REM Loop through the repositories array
for %%r in %repositories% do (
    REM check if the repository exists then skip
    if exist %%r (
        echo %%r already exists
    )else (
        git clone %base_url%%%~r.git
    )
)
REM call run.bat
