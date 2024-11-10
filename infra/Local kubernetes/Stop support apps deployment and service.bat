@echo off
start cmd /k "kubectl delete deploy kafka grafana loki tempo prometheus && kubectl delete svc kafka grafana loki tempo prometheus"
