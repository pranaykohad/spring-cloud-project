@echo off
start cmd /k "cd ../../angular-image && docker build -t ang-service:v2 . && docker tag ang-service:v2 prakohad/ang-service:v2 && docker push prakohad/ang-service:v2"
