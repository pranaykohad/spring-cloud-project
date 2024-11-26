@echo off
start cmd /k "cd ../../notification-service && mvn clean install && mvnw spring-boot:build-image"
