@echo off
start cmd /k "kubectl apply -f application-deploy-service.yaml && kubectl apply -f ingress-controller.yaml && kubectl get all"
