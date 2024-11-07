@echo off
start cmd /k "cd ../../api-gateway && mvnw spring-boot:build-image"
start cmd /k "cd ../../customer-service && mvnw spring-boot:build-image"
start cmd /k "cd ../../event-service && mvnw spring-boot:build-image"
