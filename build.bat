@echo off
REM Windows wrapper to run the Bash build script

echo Running build.sh using Git Bash...

REM Ensure Git Bash exists
SET "BASH_PATH=C:\Program Files\Git\bin\bash.exe"

IF NOT EXIST "%BASH_PATH%" (
    echo Could not find Git Bash at "%BASH_PATH%"
    echo Please install Git for Windows or adjust path.
    exit /b 1
)

"%BASH_PATH%" "%~dp0build.sh"
