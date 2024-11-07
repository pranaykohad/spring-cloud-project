@echo off
start cmd /k "kubectl apply -f application-deploy-service.yaml && kubectl get all"
