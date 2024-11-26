@echo off
start cmd /k "cd ../../event-service && mvn clean install && mvnw spring-boot:build-image"
