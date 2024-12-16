@echo off
start cmd /k "kubectl port-forward svc/prometheus 9090:9090"
timeout /t 1 >nul
start cmd /k "kubectl port-forward service/loki 3100:3100"
timeout /t 1 >nul
start cmd /k "kubectl port-forward service/grafana 3000:3000"
timeout /t 1 >nul
start cmd /k "kubectl port-forward service/api-gateway 8083:8083"
timeout /t 1 >nul
start cmd /k "kubectl port-forward service/user-service 8081:8081"
timeout /t 1 >nul
start cmd /k "kubectl port-forward service/event-service 8082:8082"
timeout /t 1 >nul
start cmd /k "kubectl port-forward service/notification-service 8084:8084"
timeout /t 1 >nul
start cmd /k "kubectl port-forward service/admin-dashboard 4200:80"
