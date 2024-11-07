@echo off
start cmd /k "kubectl delete deploy grafana loki tempo prometheus kafka && kubectl delete svc grafana loki tempo prometheus kafka"
