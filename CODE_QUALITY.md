# 🔍 Code Quality Standards

This document outlines the comprehensive code quality standards and tools used in the Ingenzi Certificate Invoicing System (StockM Backend) project.

## 📋 Overview

The project enforces strict code quality standards through automated tools and processes to ensure:
- **Consistency** - Uniform code style across all modules
- **Maintainability** - Clean, readable, and well-documented code
- **Security** - Vulnerability-free dependencies and secure coding practices
- **Performance** - Optimized code with high test coverage
- **Reliability** - Bug-free code through static analysis

## 🛠️ Quality Tools Integration

### 1. Spotless - Code Formatting

**Purpose**: Automatic code formatting and style enforcement

**Configuration**: Configured in root `pom.xml` with Google Java Format

**Features**:
- Google Java Style formatting
- Import organization and cleanup
- License header management
- POM file formatting
- Markdown and YAML formatting

**Usage**:

```bash
# Check formatting
./mvnw spotless:check

# Apply formatting
./mvnw spotless:apply

# Format specific files
./mvnw spotless:apply -Dspotless.java.target="src/main/java/**/*.java"
```

**IDE Integration**:
- Install Google Java Format plugin
- Configure IDE to use project code style
- Enable format on save

### 2. Checkstyle - Coding Standards

**Purpose**: Enforce Java coding standards and conventions

**Configuration**: `checkstyle.xml` with Google Style Guide + custom rules

**Key Rules**:
- Line length: 100 characters
- Indentation: 2 spaces
- Naming conventions
- JavaDoc requirements for public APIs
- Import organization
- Whitespace and formatting rules

**Suppressions**: `checkstyle-suppressions.xml` for legitimate exceptions

**Usage**:

```bash
# Run Checkstyle analysis
./mvnw checkstyle:check

# Generate Checkstyle report
./mvnw checkstyle:checkstyle
```

### 3. PMD - Source Code Analysis

**Purpose**: Detect common programming flaws and code smells

**Configuration**: Standard rulesets for best practices, performance, and security

**Rulesets**:
- Best Practices
- Code Style
- Design
- Error Prone
- Performance
- Security

**Usage**:

```bash
# Run PMD analysis
./mvnw pmd:check

# Generate PMD report
./mvnw pmd:pmd

# Check for copy-paste detection
./mvnw pmd:cpd-check
```

### 4. SpotBugs - Bug Pattern Detection

**Purpose**: Static analysis to find potential bugs and security vulnerabilities

**Configuration**:
- Include file: `spotbugs-include.xml`
- Exclude file: `spotbugs-exclude.xml`

**Bug Categories**:
- Security vulnerabilities
- Correctness issues
- Performance problems
- Multithreading issues
- Bad practices

**Usage**:

```bash
# Run SpotBugs analysis
./mvnw spotbugs:check

# Generate SpotBugs report
./mvnw spotbugs:spotbugs
```

### 5. JaCoCo - Code Coverage

**Purpose**: Measure and enforce test coverage standards

**Configuration**:
- Minimum coverage: 80% (configurable per profile)
- Excludes: DTOs, entities, configuration classes
- Reports: HTML and XML formats

**Coverage Types**:
- Line coverage
- Branch coverage
- Method coverage
- Class coverage

**Usage**:

```bash
# Run tests with coverage
./mvnw test

# Generate coverage report
./mvnw jacoco:report

# Check coverage thresholds
./mvnw jacoco:check
```

### 6. OWASP Dependency Check - Security Scanning

**Purpose**: Identify known security vulnerabilities in dependencies

**Configuration**: `owasp-dependency-check-suppressions.xml`

**Features**:
- CVE database scanning
- CVSS scoring
- False positive suppression
- CI/CD integration

**Usage**:

```bash
# Run security scan
./mvnw org.owasp:dependency-check-maven:check

# Generate security report
./mvnw org.owasp:dependency-check-maven:aggregate
```

## 🎯 Build Profiles

### Development Profile (default)

```bash
./mvnw clean package
```

- Spotless formatting check
- JaCoCo coverage collection
- Unit tests only
- Minimum coverage: 70%

### Production Profile

```bash
./mvnw clean package -Pprod
```

- All quality tools enabled
- Full test suite (unit + integration)
- Minimum coverage: 85%
- Security scanning
- Docker image building

### Quality Profile

```bash
./mvnw clean package -Pquality
```

- Comprehensive quality analysis
- All static analysis tools
- Detailed reporting
- Quality gate enforcement

### Quick Profile

```bash
./mvnw clean package -Pquick
```

- Skip all quality checks
- Skip tests
- Fast build for development

## 📊 Quality Metrics and Thresholds

### Code Coverage Targets

|   Component Type   | Minimum Coverage |
|--------------------|------------------|
| Service Classes    | 90%              |
| Repository Classes | 85%              |
| Controller Classes | 80%              |
| Utility Classes    | 95%              |
| DTOs/Entities      | Excluded         |

### Code Quality Thresholds

|        Metric         |  Threshold  |
|-----------------------|-------------|
| Cyclomatic Complexity | < 10        |
| Method Length         | < 50 lines  |
| Class Length          | < 500 lines |
| Parameter Count       | < 7         |
| Nested Depth          | < 4         |

### Security Standards

|         Check         |  Requirement  |
|-----------------------|---------------|
| CVSS Score            | < 8.0 (High)  |
| Known Vulnerabilities | 0 Critical    |
| Security Hotspots     | Manual Review |
| Hardcoded Secrets     | Not Allowed   |

## 🔄 Quality Gate Workflow

### Pre-commit Checks

1. **Spotless** formatting validation
2. **Unit tests** execution
3. **Basic coverage** check (70%)

### CI/CD Pipeline

1. **Build** compilation
2. **Unit tests** with coverage
3. **Integration tests**
4. **Static analysis** (Checkstyle, PMD, SpotBugs)
5. **Security scan** (OWASP)
6. **Quality gate** evaluation
7. **Artifact** generation

### Quality Gate Criteria

- ✅ All tests pass
- ✅ Coverage ≥ threshold
- ✅ No high-severity security issues
- ✅ No critical code smells
- ✅ Formatting compliance

## 🛡️ Security Standards

### Dependency Management

- Regular dependency updates
- Vulnerability scanning
- License compliance
- Supply chain security

### Code Security

- Input validation
- SQL injection prevention
- XSS protection
- Authentication/authorization
- Secure configuration

### Secrets Management

- No hardcoded secrets
- Environment-based configuration
- Encrypted sensitive data
- Secure key rotation

## 📝 Documentation Standards

### JavaDoc Requirements

- All public APIs documented
- Parameter and return descriptions
- Exception documentation
- Example usage where applicable

### Code Comments

- Explain complex business logic
- Document assumptions
- Clarify non-obvious implementations
- Avoid obvious comments

### Architecture Documentation

- Module interactions
- Design decisions
- API contracts
- Deployment guides

## 🚀 IDE Configuration

### VS Code Setup

1. Install extensions:
   - Extension Pack for Java
   - Spotless
   - SonarLint
   - Checkstyle
2. Configure settings:

   ```json
   {
     "java.format.settings.url": "./checkstyle.xml",
     "java.saveActions.organizeImports": true,
     "spotless.check.on.save": true
   }
   ```

### IntelliJ IDEA Setup

1. Install plugins:
   - Google Java Format
   - Checkstyle-IDEA
   - SonarLint
   - PMD
2. Import code style from `checkstyle.xml`
3. Enable format on save
4. Configure quality tool integrations

## 🔧 Troubleshooting

### Common Issues

**Spotless Formatting Failures**

```bash
# Fix automatically
./mvnw spotless:apply

# Check specific files
./mvnw spotless:check -Dspotless.java.target="problematic/file.java"
```

**Checkstyle Violations**

```bash
# Generate detailed report
./mvnw checkstyle:checkstyle

# View report in target/site/checkstyle.html
```

**Coverage Below Threshold**

```bash
# Check coverage report
./mvnw jacoco:report

# View report in target/site/jacoco/index.html
```

**Security Vulnerabilities**

```bash
# Update dependencies
./mvnw versions:display-dependency-updates

# Add suppressions for false positives
# Edit owasp-dependency-check-suppressions.xml
```

### Quality Tool Debugging

**Enable Verbose Output**

```bash
./mvnw clean package -Pquality -X
```

**Skip Specific Tools**

```bash
./mvnw clean package -Dcheckstyle.skip=true -Dpmd.skip=true
```

**Generate All Reports**

```bash
./mvnw clean verify site
```

## 📈 Continuous Improvement

### Regular Tasks

- Weekly dependency updates
- Monthly quality metrics review
- Quarterly tool version updates
- Annual standards review

### Quality Metrics Tracking

- Code coverage trends
- Security vulnerability counts
- Code smell evolution
- Test execution performance

### Tool Updates

- Monitor tool releases
- Evaluate new quality tools
- Update configurations
- Train team on changes

---

**Remember**: Quality is not just about tools—it's about writing clean, maintainable, and secure code that serves our users well.
