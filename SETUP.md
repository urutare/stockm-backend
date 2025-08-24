# 🚀 Ingenzi CIS Setup Guide

This guide provides comprehensive setup instructions for the Ingenzi Certificate Invoicing System (StockM Backend) with improved code quality standards and automated linting.

## 📋 Prerequisites

### Required Software

- **Java 21** (OpenJDK or Oracle JDK)
- **Maven 3.9+** (or use included Maven wrapper)
- **Docker** (for containerized development)
- **Git** (for version control)

### Recommended IDE

- **VS Code** with Java Extension Pack (see [VSCODE-SETUP.md](VSCODE-SETUP.md))
- **IntelliJ IDEA** with Maven plugin
- **Eclipse** with Maven plugin

## 🏗️ Project Setup

### 1. Clone and Initialize

```bash
# Clone the repository
git clone https://github.com/urutare/stockm-backend.git
cd stockm-backend

# Run setup script to clone all microservice dependencies
./setup.sh          # Linux/Mac
# OR
setup.bat           # Windows

# This will clone all required modules:
# - stockm-* services (core microservices)
# - POS* modules (Point of Sale system)
# - sms-gateway (Python service)
```

### 2. Environment Configuration

```bash
# Create secrets directory
mkdir -p secrets

# Copy environment template
cp .env.example secrets/.env

# Edit with your configuration
# See .env.example for required variables
nano secrets/.env   # or use your preferred editor
```

### 3. Code Quality Setup

The project now includes comprehensive code quality tools:

#### **Spotless** - Automatic Code Formatting

```bash
# Check formatting compliance
./mvnw spotless:check

# Apply automatic formatting
./mvnw spotless:apply
```

#### **Static Analysis Tools**

- **Checkstyle** - Java coding standards
- **PMD** - Source code analysis
- **SpotBugs** - Bug pattern detection
- **JaCoCo** - Code coverage
- **OWASP** - Security vulnerability scanning

## 🔧 Development Workflow

### Build Profiles

Choose the appropriate profile for your needs:

#### Development (Default)

```bash
# Fast build for development
./mvnw clean package

# Includes:
# - Spotless formatting check
# - Unit tests only
# - Basic coverage collection
```

#### Production

```bash
# Full quality build
./mvnw clean package -Pprod

# Includes:
# - All quality tools
# - Unit + integration tests
# - Security scanning
# - 85% coverage requirement
```

#### Quality Analysis

```bash
# Comprehensive quality check
./mvnw clean package -Pquality

# Runs all static analysis tools
# Generates detailed reports
```

#### Quick Build

```bash
# Skip tests and quality checks
./mvnw clean package -Pquick

# Use only for rapid iteration
```

### Code Formatting

#### Automatic Formatting

```bash
# Format all files
./mvnw spotless:apply

# Check formatting only
./mvnw spotless:check

# Format specific file types
./mvnw spotless:apply -Dspotless.java.target="src/**/*.java"
```

#### IDE Integration

Configure your IDE to use the project's formatting standards:

**VS Code:**
1. Install "Spotless" extension
2. Enable format on save
3. Use project's checkstyle.xml

**IntelliJ IDEA:**
1. Import checkstyle.xml as code style
2. Install Google Java Format plugin
3. Enable format on save

### Testing Strategy

```bash
# Unit tests only
./mvnw test

# Integration tests
./mvnw failsafe:integration-test

# All tests with coverage
./mvnw verify

# Coverage report
./mvnw jacoco:report
# View at: target/site/jacoco/index.html
```

### Static Analysis

```bash
# Run individual tools
./mvnw checkstyle:check      # Coding standards
./mvnw pmd:check             # Code analysis
./mvnw spotbugs:check        # Bug detection

# Security scanning
./mvnw org.owasp:dependency-check-maven:check

# Generate all reports
./mvnw site
```

## 🐳 Docker Development

### Quick Start

```bash
# Start all services
make dev
# OR
docker-compose -f docker-compose.prod.yaml up --build

# View logs
make logs

# Stop services
make down
```

### Service Management

```bash
# Start specific service
docker-compose up stockm-auth-service

# Scale services
docker-compose up --scale stockm-stock-service=3

# View service logs
docker-compose logs -f stockm-api-gateway
```

## 📊 Quality Gates

The project enforces quality standards through automated gates:

### Code Coverage Thresholds

- **Development**: 70% minimum
- **Production**: 85% minimum
- **Critical Services**: 90% minimum

### Security Standards

- **No high-severity vulnerabilities** (CVSS > 8.0)
- **No hardcoded secrets**
- **Dependency vulnerability scanning**

### Code Style

- **Google Java Style** formatting
- **Checkstyle** compliance
- **PMD** rule compliance
- **SpotBugs** clean analysis

## 🔄 CI/CD Integration

### GitHub Actions

The project includes automated workflows:

- **Quality Check** - Runs on every PR
- **Release** - Automated releases and deployments
- **Security Scan** - Weekly vulnerability scanning

### Local Pre-commit Validation

```bash
# Run full quality check before commit
./mvnw clean verify -Pquality

# Quick pre-commit check
./mvnw spotless:check test
```

## 🚨 Troubleshooting

### Common Issues

#### **Setup Script Fails**

```bash
# Check network connectivity
ping github.com

# Manual clone if needed
git clone https://github.com/urutare/stockm-auth-service.git

# Verify all modules exist
ls -la *stockm* *POS*
```

#### **Java Version Issues**

```bash
# Check Java version
java -version

# Should show Java 21
# Use provided setup scripts if needed:
./setup-java.ps1   # Windows PowerShell
./setup-java.bat   # Windows Command Prompt
```

#### **Maven Build Failures**

```bash
# Clean and retry
./mvnw clean

# Skip tests for quick build
./mvnw clean package -DskipTests

# Use quick profile
./mvnw clean package -Pquick
```

#### **Code Formatting Issues**

```bash
# Apply automatic fixes
./mvnw spotless:apply

# Check specific files
./mvnw spotless:check -Dspotless.java.target="problematic/file.java"
```

#### **Coverage Below Threshold**

```bash
# Generate coverage report
./mvnw jacoco:report

# View detailed report
open target/site/jacoco/index.html

# Lower threshold for development
./mvnw clean package -Pdev
```

### IDE-Specific Issues

#### **VS Code Java Issues**

```bash
# Reload VS Code window
Ctrl+Shift+P -> "Developer: Reload Window"

# Rebuild Java projects
Ctrl+Shift+P -> "Java: Rebuild Projects"

# Check Java runtime
Ctrl+Shift+P -> "Java: Show Runtime Status"
```

#### **Maven Import Issues**

```bash
# Refresh Maven dependencies
./mvnw dependency:resolve

# Force update snapshots
./mvnw clean install -U
```

## 📚 Documentation

### Available Guides

- **[README.md](README.md)** - Project overview and quick start
- **[CODE_QUALITY.md](CODE_QUALITY.md)** - Detailed quality standards
- **[VSCODE-SETUP.md](VSCODE-SETUP.md)** - VS Code development setup
- **[Architecture Guide](Writerside/)** - System architecture details

### API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html (after starting services)
- **Service Discovery**: http://localhost:8761 (Eureka dashboard)

## 🆘 Getting Help

### Resources

- **Issues**: [GitHub Issues](https://github.com/urutare/stockm-backend/issues)
- **Discussions**: [GitHub Discussions](https://github.com/urutare/stockm-backend/discussions)
- **Wiki**: [Project Wiki](https://github.com/urutare/stockm-backend/wiki)

### Quick Commands Reference

```bash
# Essential setup
./setup.sh && mkdir -p secrets && cp .env.example secrets/.env

# Development build
./mvnw clean package

# Quality check
./mvnw spotless:apply && ./mvnw verify -Pquality

# Docker development
make dev && make logs

# Release build
./mvnw clean package -Pprod
```

---

**Happy coding! 🚀**
