#!/usr/bin/env pwsh
# VS Code Setup Script for Ingenzi CIS
# This script helps configure VS Code properly for the multi-module Maven project

Write-Host "🚀 Setting up VS Code for Ingenzi CIS..." -ForegroundColor Green

# Check Java version
Write-Host "📋 Checking Java installation..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "✅ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Java not found. Please install Java 21 first!" -ForegroundColor Red
    exit 1
}

# Check Maven version  
Write-Host "📋 Checking Maven installation..." -ForegroundColor Yellow
try {
    $mavenVersion = mvn -version 2>&1 | Select-String "Apache Maven"
    Write-Host "✅ Maven found: $mavenVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Maven not found. Please install Maven first!" -ForegroundColor Red
    exit 1
}

# Clean and compile the project
Write-Host "🔧 Cleaning and compiling the project..." -ForegroundColor Yellow
try {
    mvn clean compile -DskipTests
    Write-Host "✅ Project compilation successful!" -ForegroundColor Green
} catch {
    Write-Host "⚠️  Compilation had warnings but may still work" -ForegroundColor Orange
}

# Check if workspace file exists
$workspaceFile = "ingenzi-cis.code-workspace"
if (Test-Path $workspaceFile) {
    Write-Host "✅ Workspace file found: $workspaceFile" -ForegroundColor Green
} else {
    Write-Host "❌ Workspace file not found!" -ForegroundColor Red
}

Write-Host ""
Write-Host "🎯 Next Steps:" -ForegroundColor Cyan
Write-Host "1. Close any open VS Code windows for this project" -ForegroundColor White
Write-Host "2. Open VS Code using: code $workspaceFile" -ForegroundColor White
Write-Host "3. Install recommended extensions when prompted" -ForegroundColor White
Write-Host "4. Wait for Java Language Server to initialize" -ForegroundColor White
Write-Host "5. Press Ctrl+Shift+P and run 'Java: Rebuild Projects'" -ForegroundColor White
Write-Host ""
Write-Host "🔥 Your Ingenzi CIS project should now work in VS Code!" -ForegroundColor Green
