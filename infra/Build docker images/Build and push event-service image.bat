@echo off
start cmd /k "cd ../../customer-service && mvn clean install && mvnw spring-boot:build-image"
