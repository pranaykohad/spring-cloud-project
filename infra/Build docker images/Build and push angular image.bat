@echo off
start cmd /k "cd ../../admin-dashboard && docker build -t admin-dashboard:v1 . && docker tag admin-dashboard:v1 prakohad/admin-dashboard:v1 && docker push prakohad/admin-dashboard:v1"
