@echo off
start cmd /k "kubectl delete deploy api-gateway user-service event-service admin-dashboard && kubectl delete svc api-gateway user-service event-service admin-dashboard"
