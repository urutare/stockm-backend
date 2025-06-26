@echo off
echo Installing Java 21 for Ingenzi CIS Project...
echo.

REM Check if Chocolatey is installed
choco --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Chocolatey is not installed. Installing Chocolatey first...
    echo Please run this as Administrator if the installation fails.
    echo.
    
    REM Install Chocolatey
    powershell -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
    
    if %errorlevel% neq 0 (
        echo Failed to install Chocolatey. Please install Java 21 manually from https://adoptium.net/
        echo Then restart VS Code and try again.
        pause
        exit /b 1
    )
    
    echo Chocolatey installed successfully!
    echo Please restart this terminal and run the script again.
    pause
    exit /b 0
)

echo Chocolatey is available. Installing Eclipse Temurin JDK 21...
choco install temurin21 -y

if %errorlevel% neq 0 (
    echo Failed to install Java 21 via Chocolatey.
    echo Please install Java 21 manually from https://adoptium.net/
    echo Make sure to:
    echo 1. Download Eclipse Temurin JDK 21
    echo 2. Install it
    echo 3. Set JAVA_HOME environment variable
    echo 4. Add Java to PATH
    pause
    exit /b 1
)

echo.
echo Java 21 installation completed!
echo.
echo Please:
echo 1. Restart VS Code
echo 2. Open the ingenzi-cis.code-workspace file
echo 3. Install the recommended Java extensions when prompted
echo 4. Run "Java: Rebuild Projects" from Command Palette (Ctrl+Shift+P)
echo.
pause
