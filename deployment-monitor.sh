#!/bin/bash

# Deployment Status Monitor
# This script monitors GitHub Actions workflow status and provides deployment tracking

set -e

# Configuration
REPO_OWNER="${REPO_OWNER:-urutare}"
REPO_NAME="${REPO_NAME:-stockm-backend}"
GITHUB_TOKEN="${GITHUB_TOKEN:-}"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m'

# GitHub API endpoints
API_BASE="https://api.github.com/repos/${REPO_OWNER}/${REPO_NAME}"
WORKFLOWS_API="${API_BASE}/actions/workflows"
RUNS_API="${API_BASE}/actions/runs"

# Function to make authenticated GitHub API requests
github_api() {
    local endpoint=$1
    local auth_header=""
    
    if [ -n "$GITHUB_TOKEN" ]; then
        auth_header="-H \"Authorization: token $GITHUB_TOKEN\""
    fi
    
    eval "curl -s $auth_header \"$endpoint\""
}

# Function to get workflow status
get_workflow_status() {
    local workflow_file=$1
    local branch=${2:-main}
    
    # Get workflow ID
    local workflow_info=$(github_api "$WORKFLOWS_API")
    local workflow_id=$(echo "$workflow_info" | jq -r ".workflows[] | select(.path == \".github/workflows/$workflow_file\") | .id")
    
    if [ "$workflow_id" = "null" ] || [ -z "$workflow_id" ]; then
        echo "unknown"
        return 1
    fi
    
    # Get latest run for the workflow
    local runs_info=$(github_api "$WORKFLOWS_API/$workflow_id/runs?branch=$branch&per_page=1")
    local latest_run=$(echo "$runs_info" | jq -r '.workflow_runs[0]')
    
    if [ "$latest_run" = "null" ]; then
        echo "no_runs"
        return 1
    fi
    
    local status=$(echo "$latest_run" | jq -r '.status')
    local conclusion=$(echo "$latest_run" | jq -r '.conclusion')
    local run_id=$(echo "$latest_run" | jq -r '.id')
    local created_at=$(echo "$latest_run" | jq -r '.created_at')
    local head_sha=$(echo "$latest_run" | jq -r '.head_sha')
    
    # Format the response
    cat << EOF
{
  "status": "$status",
  "conclusion": "$conclusion",
  "run_id": "$run_id",
  "created_at": "$created_at",
  "head_sha": "$head_sha"
}
EOF
}

# Function to format status with colors
format_status() {
    local status=$1
    local conclusion=$2
    
    case "$status" in
        "completed")
            case "$conclusion" in
                "success") echo -e "${GREEN}✅ Success${NC}" ;;
                "failure") echo -e "${RED}❌ Failed${NC}" ;;
                "cancelled") echo -e "${YELLOW}⏹️  Cancelled${NC}" ;;
                "skipped") echo -e "${BLUE}⏭️  Skipped${NC}" ;;
                *) echo -e "${PURPLE}❓ $conclusion${NC}" ;;
            esac
            ;;
        "in_progress") echo -e "${BLUE}🔄 In Progress${NC}" ;;
        "queued") echo -e "${YELLOW}⏳ Queued${NC}" ;;
        "requested") echo -e "${YELLOW}📝 Requested${NC}" ;;
        *) echo -e "${PURPLE}❓ $status${NC}" ;;
    esac
}

# Function to show deployment dashboard
show_dashboard() {
    echo -e "${BLUE}===========================================${NC}"
    echo -e "${BLUE}     StockM Backend Deployment Status${NC}"
    echo -e "${BLUE}===========================================${NC}"
    
    # Check production deployment
    echo -e "\n${PURPLE}🏭 Production Deployment (main branch):${NC}"
    prod_status=$(get_workflow_status "deploy-production.yml" "main" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        prod_status_text=$(echo "$prod_status" | jq -r '.status')
        prod_conclusion=$(echo "$prod_status" | jq -r '.conclusion')
        prod_run_id=$(echo "$prod_status" | jq -r '.run_id')
        prod_created=$(echo "$prod_status" | jq -r '.created_at')
        prod_sha=$(echo "$prod_status" | jq -r '.head_sha' | cut -c1-7)
        
        echo -e "   Status: $(format_status "$prod_status_text" "$prod_conclusion")"
        echo -e "   Run ID: $prod_run_id"
        echo -e "   Commit: $prod_sha"
        echo -e "   Time: $prod_created"
        echo -e "   URL: https://github.com/${REPO_OWNER}/${REPO_NAME}/actions/runs/$prod_run_id"
    else
        echo -e "   ${RED}❌ Unable to fetch production status${NC}"
    fi
    
    # Check development deployment
    echo -e "\n${PURPLE}🧪 Development Deployment (dev branch):${NC}"
    dev_status=$(get_workflow_status "deploy-development.yml" "dev" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        dev_status_text=$(echo "$dev_status" | jq -r '.status')
        dev_conclusion=$(echo "$dev_status" | jq -r '.conclusion')
        dev_run_id=$(echo "$dev_status" | jq -r '.run_id')
        dev_created=$(echo "$dev_status" | jq -r '.created_at')
        dev_sha=$(echo "$dev_status" | jq -r '.head_sha' | cut -c1-7)
        
        echo -e "   Status: $(format_status "$dev_status_text" "$dev_conclusion")"
        echo -e "   Run ID: $dev_run_id"
        echo -e "   Commit: $dev_sha"
        echo -e "   Time: $dev_created"
        echo -e "   URL: https://github.com/${REPO_OWNER}/${REPO_NAME}/actions/runs/$dev_run_id"
    else
        echo -e "   ${RED}❌ Unable to fetch development status${NC}"
    fi
}

# Function to monitor workflow in real-time
monitor_workflow() {
    local workflow_file=$1
    local branch=$2
    local check_interval=${3:-30}
    
    echo -e "${BLUE}🔍 Monitoring workflow: $workflow_file on $branch branch${NC}"
    echo -e "${BLUE}Press Ctrl+C to stop monitoring${NC}\n"
    
    while true; do
        local status_info=$(get_workflow_status "$workflow_file" "$branch" 2>/dev/null)
        
        if [ $? -eq 0 ]; then
            local status=$(echo "$status_info" | jq -r '.status')
            local conclusion=$(echo "$status_info" | jq -r '.conclusion')
            local run_id=$(echo "$status_info" | jq -r '.run_id')
            
            echo -e "$(date '+%Y-%m-%d %H:%M:%S') - $(format_status "$status" "$conclusion") (Run: $run_id)"
            
            # Break if workflow is completed
            if [ "$status" = "completed" ]; then
                echo -e "\n${GREEN}🎉 Workflow completed with status: $conclusion${NC}"
                break
            fi
        else
            echo -e "$(date '+%Y-%m-%d %H:%M:%S') - ${RED}❌ Unable to fetch status${NC}"
        fi
        
        sleep $check_interval
    done
}

# Function to show help
show_help() {
    cat << EOF
StockM Backend Deployment Status Monitor

Usage: $0 [COMMAND] [OPTIONS]

COMMANDS:
  dashboard              Show deployment status dashboard (default)
  monitor WORKFLOW       Monitor specific workflow in real-time
  status WORKFLOW        Get status of specific workflow
  
WORKFLOWS:
  production, prod       deploy-production.yml
  development, dev       deploy-development.yml

OPTIONS:
  -h, --help            Show this help message
  -b, --branch BRANCH   Specify branch (default: main for prod, dev for dev)
  -i, --interval SEC    Monitor check interval in seconds (default: 30)
  -r, --repo OWNER/NAME Repository (default: urutare/stockm-backend)
  -t, --token TOKEN     GitHub API token

ENVIRONMENT VARIABLES:
  GITHUB_TOKEN          GitHub API token for authentication
  REPO_OWNER            Repository owner (default: urutare)
  REPO_NAME             Repository name (default: stockm-backend)

EXAMPLES:
  $0                                    # Show dashboard
  $0 dashboard                          # Show dashboard
  $0 status production                  # Get production workflow status
  $0 monitor dev -i 10                  # Monitor dev workflow every 10 seconds
  $0 -t ghp_xxx dashboard               # Use specific GitHub token

NOTES:
  - GitHub token is recommended for higher API rate limits
  - Create token at: https://github.com/settings/tokens
  - Token needs 'repo' or 'public_repo' permissions
EOF
}

# Parse command line arguments
COMMAND="dashboard"
WORKFLOW=""
BRANCH=""
INTERVAL=30

while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -b|--branch)
            BRANCH="$2"
            shift 2
            ;;
        -i|--interval)
            INTERVAL="$2"
            shift 2
            ;;
        -r|--repo)
            IFS='/' read -r REPO_OWNER REPO_NAME <<< "$2"
            shift 2
            ;;
        -t|--token)
            GITHUB_TOKEN="$2"
            shift 2
            ;;
        dashboard)
            COMMAND="dashboard"
            shift
            ;;
        monitor)
            COMMAND="monitor"
            WORKFLOW="$2"
            shift 2
            ;;
        status)
            COMMAND="status"
            WORKFLOW="$2"
            shift 2
            ;;
        production|prod)
            WORKFLOW="deploy-production.yml"
            BRANCH="${BRANCH:-main}"
            shift
            ;;
        development|dev)
            WORKFLOW="deploy-development.yml"
            BRANCH="${BRANCH:-dev}"
            shift
            ;;
        *)
            echo "Unknown option: $1"
            show_help
            exit 1
            ;;
    esac
done

# Check dependencies
if ! command -v jq >/dev/null 2>&1; then
    echo -e "${RED}❌ Error: jq is required but not installed${NC}"
    echo "Install with: sudo apt-get install jq (Ubuntu/Debian) or brew install jq (macOS)"
    exit 1
fi

if ! command -v curl >/dev/null 2>&1; then
    echo -e "${RED}❌ Error: curl is required but not installed${NC}"
    exit 1
fi

# Execute command
case $COMMAND in
    dashboard)
        show_dashboard
        ;;
    monitor)
        if [ -z "$WORKFLOW" ]; then
            echo -e "${RED}❌ Error: Workflow must be specified for monitor command${NC}"
            exit 1
        fi
        monitor_workflow "$WORKFLOW" "$BRANCH" "$INTERVAL"
        ;;
    status)
        if [ -z "$WORKFLOW" ]; then
            echo -e "${RED}❌ Error: Workflow must be specified for status command${NC}"
            exit 1
        fi
        status_info=$(get_workflow_status "$WORKFLOW" "$BRANCH")
        if [ $? -eq 0 ]; then
            echo "$status_info" | jq '.'
        else
            echo -e "${RED}❌ Unable to fetch workflow status${NC}"
            exit 1
        fi
        ;;
esac