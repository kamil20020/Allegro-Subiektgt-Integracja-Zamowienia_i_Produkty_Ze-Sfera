@echo off
cd /d "%~dp0"
php\php.exe -c php\php.ini-production -S localhost:80 -t htdocs
pause