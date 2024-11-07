@echo off
start cmd /k "kubectl apply -f support-apps-deploy-service.yaml && kubectl get all"
