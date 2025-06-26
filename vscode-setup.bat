@echo off
echo Setting up VS Code for Ingenzi CIS...
echo.

echo Checking Java installation...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java not found. Please install Java 21 first!
    pause
    exit /b 1
)

echo.
echo Checking Maven installation...
mvn -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven not found. Please install Maven first!
    pause
    exit /b 1
)

echo.
echo Cleaning and compiling the project...
mvn clean compile -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: Compilation had issues but may still work
)

echo.
echo Checking workspace file...
if exist "ingenzi-cis.code-workspace" (
    echo ✅ Workspace file found
) else (
    echo ❌ Workspace file not found!
)

echo.
echo ==========================================
echo Next Steps:
echo 1. Close any open VS Code windows for this project
echo 2. Open VS Code using: code ingenzi-cis.code-workspace
echo 3. Install recommended extensions when prompted
echo 4. Wait for Java Language Server to initialize
echo 5. Press Ctrl+Shift+P and run 'Java: Rebuild Projects'
echo ==========================================
echo.
echo Your Ingenzi CIS project should now work in VS Code!
pause
