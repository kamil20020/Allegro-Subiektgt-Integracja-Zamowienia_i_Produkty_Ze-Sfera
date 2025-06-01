@echo off
cd /d "%~dp0"
php\php.exe -c php\php.ini-production -S 127.0.0.1:80 -t htdocs
pause