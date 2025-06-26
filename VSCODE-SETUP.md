# VS Code Setup for Ingenzi CIS

This guide will help you set up VS Code to work seamlessly with the Ingenzi Certificate Invoicing System project, eliminating the errors you're experiencing.

## Quick Setup (Recommended)

### Step 1: Install Java 21

Run one of these commands as Administrator:

**Option A: Using PowerShell (Recommended)**

```powershell
# Run PowerShell as Administrator, then:
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
.\setup-java.ps1
```

**Option B: Using Command Prompt**

```cmd
# Run as Administrator
setup-java.bat
```

**Option C: Manual Installation**

1. Download Eclipse Temurin JDK 21 from: https://adoptium.net/temurin/releases/?version=21
2. Install and make sure to check "Set JAVA_HOME environment variable"
3. Restart your computer

### Step 2: Open Project in VS Code

1. **Important**: Don't open the folder directly
2. Instead, open the `ingenzi-cis.code-workspace` file
3. VS Code will prompt to install recommended extensions - click "Install"

### Step 3: Let VS Code Initialize

1. Wait for the Java Language Server to start (check bottom status bar)
2. Open Command Palette (`Ctrl+Shift+P`)
3. Run: `Java: Rebuild Projects`
4. Wait for the build to complete

## What This Setup Provides

### 🔧 **Proper Configuration**

- Multi-module Maven project support
- Custom framework class recognition
- Optimized memory settings for large projects
- Automatic dependency resolution

### 🚀 **Enhanced Features**

- **Debugging**: Pre-configured launch configs for each microservice
- **Building**: Maven tasks for clean, compile, test
- **Docker**: Ready-to-use Docker Compose tasks
- **Code Navigation**: Full IntelliSense for your custom POS classes

### 📁 **Project Structure Support**

The workspace properly handles:

- Core Services (stockm-\*)
- POS Services (POS\*)
- Python SMS Gateway
- Shared libraries and dependencies

## VS Code Extensions (Auto-installed)

These extensions will be automatically installed:

- **Language Support for Java** - Core Java support
- **Debugger for Java** - Debugging capabilities
- **Maven for Java** - Maven project management
- **Extension Pack for Java** - Complete Java tooling
- **Spring Boot Tools** - Spring Boot support
- **Spring Boot Extension Pack** - Additional Spring tools

## Troubleshooting

### "Cannot resolve import" errors

1. Press `Ctrl+Shift+P`
2. Run `Java: Reload Projects`
3. Run `Java: Rebuild Projects`

### Build failures

1. Open terminal in VS Code
2. Run: `mvn clean compile -DskipTests`
3. If Maven not found, restart VS Code after Java installation

### Memory issues with large project

The settings already include optimized JVM args:

```
-XX:+UseParallelGC -Xmx2G -Xms100m
```

### IntelliSense not working

1. Ensure all extensions are installed and enabled
2. Check Java Language Server status (bottom status bar)
3. Try: `Java: Clean Workspace` then `Java: Rebuild Projects`

## Key Features Enabled

### 🎯 **Smart Code Completion**

- Full IntelliSense for Spring Boot annotations
- Custom POS framework classes
- Maven dependencies
- Database entities and DTOs

### 🔍 **Advanced Navigation**

- Go to definition across modules
- Find all references
- Type hierarchy
- Call hierarchy

### 🐛 **Debugging**

- Breakpoints in any service
- Step through microservice calls
- Variable inspection
- Hot code replacement

### ⚡ **Build Integration**

- Automatic compilation on save
- Maven goal execution
- Docker container management
- Test runner integration

## Project-Specific Settings

The configuration includes optimizations for:

- **Large Codebases**: Memory optimization and build caching
- **Microservices**: Multi-module project handling
- **Spring Boot**: Auto-configuration and property support
- **Custom Frameworks**: Recognition of POS\* modules
- **Database Integration**: JPA entity support

## Next Steps

After setup completion:

1. Open any Java file to test IntelliSense
2. Try debugging a service using the pre-configured launch configs
3. Use `Ctrl+Shift+P` → `Tasks: Run Task` to access Maven and Docker tasks
4. Explore the Spring Boot dashboard for application management

## Comparison with IntelliJ IDEA

This VS Code setup provides equivalent functionality to IntelliJ IDEA:

- ✅ Multi-module Maven support
- ✅ Spring Boot integration
- ✅ Advanced refactoring
- ✅ Database tools integration
- ✅ Git integration
- ✅ Docker support
- ✅ Custom framework recognition

The main advantage is the lighter resource usage while maintaining professional Java development capabilities.

---

**Need Help?** Check the VS Code Java documentation or run `Java: Get Help` from the Command Palette.
