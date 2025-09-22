# GitHub Actions Deployment Setup for StockM Backend

This document explains how to configure GitHub Actions workflows for deploying the StockM Backend multi-repository system to DigitalOcean.

## Overview

The deployment system includes two main workflows:

1. **Production Deployment** (`deploy-production.yml`) - Triggered on pushes to `main` branch
2. **Development Deployment** (`deploy-development.yml`) - Triggered on pushes to `develop` branch

## Key Features

### 🚀 **Multi-Repository Support**
- Automatically clones all 27+ required repositories using `setup.sh`
- Handles dependencies between stockm-*, POS*, and sms-gateway modules
- Intelligent caching to reduce setup time

### 🏗️ **Efficient Build Process**
- Parallel Maven builds with resource optimization
- Docker multi-stage builds for minimal image sizes
- Separate build and deployment phases for better tracking

### 🔒 **Security & Secrets Management**
- Separate environment configurations for production and development
- Secure handling of sensitive credentials
- DigitalOcean Container Registry integration

### 📊 **Resource Optimization**
- Build caching for repositories and Maven dependencies
- Parallel service builds using matrix strategy
- Development workflow builds only core services for faster deployment

## Required GitHub Secrets

### Repository Settings
Go to your GitHub repository → Settings → Secrets and variables → Actions → New repository secret

### Production Environment Secrets

#### DigitalOcean Configuration
```
DO_REGISTRY_TOKEN          # DigitalOcean Container Registry token
DO_HOST                    # Production droplet IP address
DO_USERNAME                # SSH username (usually 'root' or 'deploy')
DO_SSH_PRIVATE_KEY         # Private SSH key for droplet access
DO_SSH_PORT                # SSH port (default: 22)
```

#### Application Configuration
```
SERVER                     # Server URL/domain
EUREKA_SERVER_URL          # Eureka discovery server URL
EUREKA_INSTANCE_URL        # Eureka instance URL

# Database
DB_HOSTNAME                # PostgreSQL hostname
DB_PORT                    # PostgreSQL port
DB_USER                    # Database username
DB_PASS                    # Database password

# Redis
REDIS_HOST                 # Redis hostname
REDIS_PORT                 # Redis port

# JWT Configuration
SECRET_KEY                 # Application secret key
JWT_SECRET_KEY             # JWT signing key
JWT_ACCESS_TOKEN_EXPIRATION_IN_MS    # Access token expiration
JWT_REFRESH_TOKEN_EXPIRATION_IN_MS   # Refresh token expiration

# SMS Services
PINDO_TOKEN                # Pindo SMS API token
PINDO_URL                  # Pindo API URL
AFRICAS_TALKING_TOKEN      # Africa's Talking API token
AFRICAS_TALKING_USERNAME   # Africa's Talking username

# Email Configuration
EMAIL_HOST                 # SMTP host
EMAIL_PORT                 # SMTP port
EMAIL_USERNAME             # Email username
EMAIL_PASSWORD             # Email password

# Cloudinary (File Storage)
CLOUDINARY_CLOUD_NAME      # Cloudinary cloud name
CLOUDINARY_API_KEY         # Cloudinary API key
CLOUDINARY_API_SECRET      # Cloudinary API secret

# MTN Mobile Money
MTN_MOMO_API               # MTN MoMo API endpoint
MTN_API_USER               # MTN API user
MTN_API_KEY                # MTN API key
MTN_PRIMARY_KEY            # MTN primary subscription key
MTN_SECONDARY_KEY          # MTN secondary subscription key
MTN_TARGET_ENV             # MTN environment (production)
MTN_CALLBACK_URL           # MTN callback URL

# Service URLs
USER_SERVICE_URL           # User service URL
CATEGORY_SERVICE_URL       # Category service URL
STOCK_SERVICE_URL          # Stock service URL
SYNC_SERVICE_URL           # Sync service URL
PAYMENT_SERVICE_URL        # Payment service URL
SMS_SERVICE_URL            # SMS service URL

# Additional Configuration
STOCKM_API_SECRET_KEY      # StockM API secret
STOCKM_TOKEN_VALIDITY      # Token validity period
STOCKM_BASE_URL            # StockM base URL
```

### Development Environment Secrets

For development environment, add the same secrets with `DEV_` prefix:

```
DEV_DO_HOST                # Development droplet IP
DEV_DO_USERNAME            # Development SSH username
DEV_DO_SSH_PRIVATE_KEY     # Development SSH private key
DEV_DO_SSH_PORT            # Development SSH port

DEV_SERVER                 # Development server URL
DEV_EUREKA_SERVER_URL      # Development Eureka URL
DEV_EUREKA_INSTANCE_URL    # Development Eureka instance URL
DEV_DB_HOSTNAME            # Development database host
DEV_DB_PORT                # Development database port
DEV_DB_USER                # Development database user
DEV_DB_PASS                # Development database password
# ... (continue with DEV_ prefix for all other secrets)
```

## DigitalOcean Setup

### 1. Container Registry Setup

```bash
# Create container registry in DigitalOcean
doctl registry create stockm-backend --region nyc3

# Create development registry
doctl registry create stockm-backend-dev --region nyc3

# Get registry token
doctl registry kubernetes-manifest | kubectl apply -f -
```

### 2. Droplet Configuration

#### Production Droplet
```bash
# Minimum requirements
# CPU: 4 cores
# RAM: 8GB
# Storage: 100GB SSD
# OS: Ubuntu 22.04 LTS

# Install Docker and Docker Compose
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh

# Install docker-compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Create deployment directory
sudo mkdir -p /opt/stockm-backend
sudo chown $USER:$USER /opt/stockm-backend
```

#### Development Droplet
```bash
# Minimum requirements (smaller for development)
# CPU: 2 cores
# RAM: 4GB
# Storage: 50GB SSD
# OS: Ubuntu 22.04 LTS

# Same Docker setup as production
# Create development deployment directory
sudo mkdir -p /opt/stockm-backend-dev
sudo chown $USER:$USER /opt/stockm-backend-dev
```

### 3. SSH Key Setup

```bash
# Generate SSH key pair
ssh-keygen -t rsa -b 4096 -C "github-actions@stockm-backend"

# Copy public key to droplets
ssh-copy-id -i ~/.ssh/id_rsa.pub root@your-production-droplet-ip
ssh-copy-id -i ~/.ssh/id_rsa.pub root@your-development-droplet-ip

# Add private key to GitHub secrets as DO_SSH_PRIVATE_KEY and DEV_DO_SSH_PRIVATE_KEY
```

## Workflow Customization

### Environment Specific Configurations

#### Production Workflow Features:
- Complete service deployment (all microservices)
- Health checks for all critical services
- Post-deployment verification
- Resource cleanup

#### Development Workflow Features:
- Core services only (faster deployment)
- Simplified health checks
- Optional integration tests
- Pull request validation

### Triggering Deployments

#### Automatic Triggers:
```bash
# Production deployment
git push origin main

# Development deployment
git push origin develop
```

#### Manual Triggers:
- Go to Actions tab in GitHub
- Select "Deploy to Production" or "Deploy to Development"
- Click "Run workflow"

## Monitoring and Troubleshooting

## Monitoring and Troubleshooting

### Service Health Endpoints

After deployment, verify services at:

```bash
# Production
http://your-production-ip:8761/actuator/health  # Discovery Server
http://your-production-ip:8888/actuator/health  # Config Server
http://your-production-ip:8080/actuator/health  # API Gateway

# Development
http://your-development-ip:8761/actuator/health
http://your-development-ip:8888/actuator/health
http://your-development-ip:8080/actuator/health
```

### Monitoring Tools

#### Health Check Script
Use the included health check script to monitor service status:

```bash
# Check all services in both environments
./health-check.sh

# Check production only
./health-check.sh prod

# Check development with custom host
./health-check.sh -d dev.example.com develop

# Verbose output with Docker status
./health-check.sh -v both
```

#### Deployment Status Monitor
Monitor GitHub Actions workflow status in real-time:

```bash
# Show deployment dashboard
./deployment-monitor.sh

# Monitor production deployment in real-time
./deployment-monitor.sh monitor production

# Get development workflow status
./deployment-monitor.sh status development

# Monitor with custom settings
GITHUB_API_TOKEN=ghp_xxx ./deployment-monitor.sh monitor develop -i 10
```

**Note**: The deployment monitor requires `jq` for JSON processing:
```bash
# Install jq
sudo apt-get install jq        # Ubuntu/Debian
brew install jq                # macOS
```

#### GitHub Token Configuration

**Important**: Do NOT create a secret named `GITHUB_TOKEN` - this is reserved by GitHub.

For the deployment workflows, `${{ secrets.GITHUB_TOKEN }}` is automatically provided by GitHub Actions.

For the monitoring tools, create a Personal Access Token:
1. Go to https://github.com/settings/tokens
2. Click "Generate new token (classic)"
3. Give it a name like "StockM Deployment Monitor"
4. Select scopes: `repo` (for private repos) or `public_repo` (for public repos)
5. Copy the token and use it as `GITHUB_API_TOKEN` environment variable

```bash
# Use with monitoring tools
export GITHUB_API_TOKEN=ghp_your_token_here
./deployment-monitor.sh dashboard

# Or pass directly
GITHUB_API_TOKEN=ghp_your_token_here ./deployment-monitor.sh monitor production
```

### Common Issues and Solutions

#### 1. Repository Cloning Failures
- **Cause**: GitHub authentication issues or rate limiting
- **Solution**: Workflows now automatically configure git authentication using the built-in `${{ secrets.GITHUB_TOKEN }}`
- **Additional**: Ensure all repositories in the organization are accessible to the token

#### 2. Docker Build Failures
- **Cause**: Missing dependencies or build context
- **Solution**: Check that all repositories were cloned successfully

#### 3. Service Health Check Failures
- **Cause**: Service startup timeouts or configuration issues
- **Solution**: Check service logs and environment configuration

#### 4. SSH Connection Issues
- **Cause**: Incorrect SSH configuration
- **Solution**: Verify SSH keys and droplet accessibility

### Debugging Commands

```bash
# Check workflow logs in GitHub Actions

# On droplet, check service status
docker compose -f docker-compose.prod.yaml ps
docker compose -f docker-compose.prod.yaml logs service-name

# Check resource usage
docker stats
df -h
free -h
```

## Security Best Practices

1. **Secret Rotation**: Regularly rotate API keys and tokens
2. **SSH Key Management**: Use dedicated SSH keys for deployment
3. **Network Security**: Configure firewall rules on droplets
4. **Container Security**: Regularly update base images
5. **Access Control**: Limit repository access to authorized users

## Workflow Optimization Tips

1. **Caching**: Repository and Maven caches reduce build time by 60-80%
2. **Parallel Builds**: Matrix strategy builds multiple services simultaneously
3. **Conditional Deployment**: Development workflow skips non-essential services
4. **Resource Limits**: Configure appropriate timeouts and resource limits

## Maintenance

### Regular Tasks:
- Monitor disk usage on droplets
- Update base Docker images
- Review and rotate secrets
- Monitor workflow execution times
- Clean up old Docker images

### Scaling Considerations:
- Add load balancers for production
- Implement blue-green deployments
- Set up monitoring and alerting
- Consider Kubernetes migration for larger scale

## Support and Documentation

- **Repository Issues**: Create GitHub issues for workflow problems
- **DigitalOcean Docs**: https://docs.digitalocean.com/
- **GitHub Actions Docs**: https://docs.github.com/en/actions
- **Docker Compose Docs**: https://docs.docker.com/compose/