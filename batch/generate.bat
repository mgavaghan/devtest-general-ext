@echo off

set CLASSPATH=..\target\devtest-general-ext-0.0.1-SNAPSHOT.jar;gavaghan-json-1.1.0.jar

java org.gavaghan.devtest.templates.App %1
