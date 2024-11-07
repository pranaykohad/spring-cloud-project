@echo off
start cmd /k "kubectl delete deploy api-gateway user-service event-service angular-app && kubectl delete svc api-gateway user-service event-service angular-app"
