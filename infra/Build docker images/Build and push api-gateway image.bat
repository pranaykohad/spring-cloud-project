@echo off
start cmd /k "cd ../../api-gateway && mvn clean install && mvnw spring-boot:build-image"
