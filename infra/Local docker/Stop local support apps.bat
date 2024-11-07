@echo off
echo Stopping Docker containers...

for /f "tokens=*" %%i in ('docker ps -q -f "name=kafka"') do set KAFKA_ID=%%i
for /f "tokens=*" %%i in ('docker ps -q -f "name=loki"') do set LOKI_ID=%%i
for /f "tokens=*" %%i in ('docker ps -q -f "name=prometheus"') do set PROMETHEUS_ID=%%i
for /f "tokens=*" %%i in ('docker ps -q -f "name=tempo"') do set TEMPO_ID=%%i
for /f "tokens=*" %%i in ('docker ps -q -f "name=grafana"') do set GRAFANA_ID=%%i

docker stop %KAFKA_ID%
docker stop %LOKI_ID%
docker stop %PROMETHEUS_ID%
docker stop %TEMPO_ID%
docker stop %GRAFANA_ID%

echo Docker containers stopped successfully.
pause