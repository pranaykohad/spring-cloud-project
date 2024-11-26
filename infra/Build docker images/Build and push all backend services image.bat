@echo off
start cmd /k "cd ../../api-gateway && mvn clean install && mvnw spring-boot:build-image"
start cmd /k "cd ../../customer-service && mvn clean install && mvnw spring-boot:build-image"
start cmd /k "cd ../../event-service && mvn clean install && mvnw spring-boot:build-image"
start cmd /k "cd ../../notification-service && mvn clean install && mvnw spring-boot:build-image"