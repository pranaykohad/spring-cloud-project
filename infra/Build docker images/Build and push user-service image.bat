@echo off
start cmd /k "cd ../../event-service && mvnw spring-boot:build-image"
