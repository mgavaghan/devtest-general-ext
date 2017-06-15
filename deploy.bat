@echo off
call mvn clean install
copy  /Y target\devtest-general-ext* "%LISA_HOME%\hotDeploy"