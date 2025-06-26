# Ingenzi CIS - Java Setup Script
Write-Host "Setting up Java 21 for Ingenzi CIS Project..." -ForegroundColor Green
Write-Host ""

# Check if Java is already installed
try {
    $javaVersion = & java -version 2>&1
    if ($javaVersion -match "21\.") {
        Write-Host "Java 21 is already installed!" -ForegroundColor Green
        Write-Host "Java version: $($javaVersion[0])" -ForegroundColor Yellow
        
        # Check JAVA_HOME
        $javaHome = [Environment]::GetEnvironmentVariable("JAVA_HOME", "Machine")
        if ($javaHome) {
            Write-Host "JAVA_HOME is set to: $javaHome" -ForegroundColor Green
        }
        else {
            Write-Host "Warning: JAVA_HOME is not set. Please set it manually." -ForegroundColor Yellow
        }
        
        Write-Host ""
        Write-Host "You can now proceed with VS Code setup:" -ForegroundColor Cyan
        Write-Host "1. Open ingenzi-cis.code-workspace file" -ForegroundColor White
        Write-Host "2. Install recommended extensions when prompted" -ForegroundColor White
        Write-Host "3. Run 'Java: Rebuild Projects' from Command Palette" -ForegroundColor White
        return
    }
}
catch {
    Write-Host "Java not found. Proceeding with installation..." -ForegroundColor Yellow
}

# Check if running as administrator
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)

if (-not $isAdmin) {
    Write-Host "This script requires administrator privileges to install Java." -ForegroundColor Red
    Write-Host "Please run PowerShell as Administrator and try again." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Alternative: Download and install Java 21 manually from:" -ForegroundColor Cyan
    Write-Host "https://adoptium.net/temurin/releases/?version=21" -ForegroundColor Blue
    Read-Host "Press Enter to exit"
    return
}

# Check for winget (Windows Package Manager)
try {
    $wingetVersion = & winget --version 2>&1
    Write-Host "Using Windows Package Manager (winget) to install Java 21..." -ForegroundColor Green
    
    # Install Eclipse Temurin JDK 21
    & winget install EclipseAdoptium.Temurin.21.JDK --accept-source-agreements --accept-package-agreements
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "Java 21 installed successfully!" -ForegroundColor Green
        Write-Host ""
        Write-Host "Please restart your terminal and VS Code, then:" -ForegroundColor Cyan
        Write-Host "1. Open ingenzi-cis.code-workspace file" -ForegroundColor White
        Write-Host "2. Install recommended extensions when prompted" -ForegroundColor White
        Write-Host "3. Run 'Java: Rebuild Projects' from Command Palette" -ForegroundColor White
    }
    else {
        Write-Host "Installation failed. Please install Java 21 manually." -ForegroundColor Red
    }
}
catch {
    Write-Host "Windows Package Manager not available." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Please install Java 21 manually:" -ForegroundColor Cyan
    Write-Host "1. Go to https://adoptium.net/temurin/releases/?version=21" -ForegroundColor Blue
    Write-Host "2. Download the Windows x64 MSI installer" -ForegroundColor White
    Write-Host "3. Run the installer and make sure to check 'Set JAVA_HOME'" -ForegroundColor White
    Write-Host "4. Restart VS Code" -ForegroundColor White
}

Read-Host "Press Enter to exit"
