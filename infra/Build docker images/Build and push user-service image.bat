@echo off
start cmd /k "cd ../../user-service && mvn clean install && mvnw spring-boot:build-image"
