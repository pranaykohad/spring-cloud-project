@echo off
start cmd /k "docker compose -f docker-compose.yaml up -d && docker ps"
