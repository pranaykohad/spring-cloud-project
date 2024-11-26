@echo off
start cmd /k "kubectl delete deploy schema-registry zookeeper broker kafka grafana loki tempo prometheus && kubectl delete svc schema-registry zookeeper broker kafka grafana loki tempo prometheus"
