@echo off
cd /d %~dp0
call mvn clean package
if errorlevel 1 exit /b 1
echo Built target\TableBook.war. Deploy it to Apache Tomcat 10.1+ webapps.
