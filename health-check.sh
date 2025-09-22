#!/bin/bash

# StockM Backend Health Check Script
# This script checks the health of all deployed services

set -e

# Configuration
PROD_HOST="${PROD_HOST:-localhost}"
DEV_HOST="${DEV_HOST:-localhost}"
TIMEOUT=${TIMEOUT:-10}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Service configurations
declare -A SERVICES=(
    ["discovery-server"]="8761"
    ["config-server"]="8888"
    ["api-gateway"]="8080"
    ["user-service"]="8081"
    ["category-service"]="8082"
    ["sync-service"]="8083"
    ["stock-service"]="8084"
    ["sms-gateway"]="8085"
    ["payment-service"]="8086"
)

# Function to check service health
check_service() {
    local service_name=$1
    local host=$2
    local port=$3
    local endpoint="${4:-/actuator/health}"
    
    local url="http://${host}:${port}${endpoint}"
    
    printf "%-20s " "$service_name"
    
    if curl -sf --max-time $TIMEOUT "$url" >/dev/null 2>&1; then
        printf "${GREEN}✅ HEALTHY${NC}\n"
        return 0
    else
        printf "${RED}❌ UNHEALTHY${NC}\n"
        return 1
    fi
}

# Function to check environment
check_environment() {
    local env_name=$1
    local host=$2
    
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}  $env_name Environment Health${NC}"
    echo -e "${BLUE}  Host: $host${NC}"
    echo -e "${BLUE}================================${NC}"
    
    local healthy_count=0
    local total_count=0
    
    for service in "${!SERVICES[@]}"; do
        port=${SERVICES[$service]}
        total_count=$((total_count + 1))
        
        if check_service "$service" "$host" "$port"; then
            healthy_count=$((healthy_count + 1))
        fi
    done
    
    echo -e "${BLUE}--------------------------------${NC}"
    printf "Health Summary: %d/%d services healthy\n" $healthy_count $total_count
    
    if [ $healthy_count -eq $total_count ]; then
        echo -e "${GREEN}🎉 All services are healthy!${NC}"
        return 0
    elif [ $healthy_count -gt 0 ]; then
        echo -e "${YELLOW}⚠️  Some services are unhealthy${NC}"
        return 1
    else
        echo -e "${RED}🚨 All services are down!${NC}"
        return 2
    fi
}

# Function to show help
show_help() {
    echo "StockM Backend Health Check Script"
    echo ""
    echo "Usage: $0 [OPTIONS] [ENVIRONMENT]"
    echo ""
    echo "ENVIRONMENT:"
    echo "  prod, production    Check production environment"
    echo "  dev, development    Check development environment"
    echo "  both               Check both environments (default)"
    echo ""
    echo "OPTIONS:"
    echo "  -h, --help         Show this help message"
    echo "  -t, --timeout SEC  Set timeout for health checks (default: 10)"
    echo "  -p, --prod-host    Production host (default: localhost)"
    echo "  -d, --dev-host     Development host (default: localhost)"
    echo "  -v, --verbose      Show detailed output"
    echo ""
    echo "Environment Variables:"
    echo "  PROD_HOST          Production host override"
    echo "  DEV_HOST           Development host override"
    echo "  TIMEOUT            Timeout override"
    echo ""
    echo "Examples:"
    echo "  $0                                    # Check both environments"
    echo "  $0 prod                              # Check production only"
    echo "  $0 dev                               # Check development only"
    echo "  $0 -p prod.example.com prod          # Check production with custom host"
    echo "  $0 -t 30 both                       # Check both with 30s timeout"
}

# Function to check Docker services
check_docker_services() {
    local host=$1
    local env_name=$2
    
    echo -e "\n${BLUE}Docker Services Status ($env_name):${NC}"
    
    if [ "$host" = "localhost" ] || [ "$host" = "127.0.0.1" ]; then
        # Local Docker check
        if command -v docker >/dev/null 2>&1; then
            if [ "$env_name" = "Production" ]; then
                docker compose -f docker-compose.prod.yaml ps 2>/dev/null || echo "No production containers running"
            else
                docker compose -f docker-compose.dev.yaml ps 2>/dev/null || echo "No development containers running"
            fi
        else
            echo "Docker not available locally"
        fi
    else
        echo "Remote Docker status check not implemented"
    fi
}

# Parse command line arguments
VERBOSE=false

while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -t|--timeout)
            TIMEOUT="$2"
            shift 2
            ;;
        -p|--prod-host)
            PROD_HOST="$2"
            shift 2
            ;;
        -d|--dev-host)
            DEV_HOST="$2"
            shift 2
            ;;
        -v|--verbose)
            VERBOSE=true
            shift
            ;;
        prod|production)
            ENV_MODE="prod"
            shift
            ;;
        dev|development)
            ENV_MODE="dev"
            shift
            ;;
        both)
            ENV_MODE="both"
            shift
            ;;
        *)
            echo "Unknown option: $1"
            show_help
            exit 1
            ;;
    esac
done

# Set default environment mode
ENV_MODE=${ENV_MODE:-both}

# Main execution
echo -e "${BLUE}🏥 StockM Backend Health Check${NC}"
echo -e "${BLUE}Timeout: ${TIMEOUT}s${NC}"
echo ""

exit_code=0

case $ENV_MODE in
    prod)
        check_environment "Production" "$PROD_HOST"
        prod_result=$?
        [ $prod_result -ne 0 ] && exit_code=$prod_result
        
        if [ "$VERBOSE" = true ]; then
            check_docker_services "$PROD_HOST" "Production"
        fi
        ;;
    dev)
        check_environment "Development" "$DEV_HOST"
        dev_result=$?
        [ $dev_result -ne 0 ] && exit_code=$dev_result
        
        if [ "$VERBOSE" = true ]; then
            check_docker_services "$DEV_HOST" "Development"
        fi
        ;;
    both)
        check_environment "Production" "$PROD_HOST"
        prod_result=$?
        
        echo ""
        
        check_environment "Development" "$DEV_HOST"
        dev_result=$?
        
        # Set exit code based on worst result
        if [ $prod_result -ne 0 ] || [ $dev_result -ne 0 ]; then
            if [ $prod_result -eq 2 ] || [ $dev_result -eq 2 ]; then
                exit_code=2
            else
                exit_code=1
            fi
        fi
        
        if [ "$VERBOSE" = true ]; then
            check_docker_services "$PROD_HOST" "Production"
            check_docker_services "$DEV_HOST" "Development"
        fi
        ;;
esac

echo ""
case $exit_code in
    0)
        echo -e "${GREEN}🎉 Overall Status: All systems operational${NC}"
        ;;
    1)
        echo -e "${YELLOW}⚠️  Overall Status: Some issues detected${NC}"
        ;;
    2)
        echo -e "${RED}🚨 Overall Status: Critical issues detected${NC}"
        ;;
esac

exit $exit_code