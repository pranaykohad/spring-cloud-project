@echo off
start cmd /k "cd ../../kubernetes-service && mvn clean install && cd target && java -jar kubernetes-service-0.0.1-SNAPSHOT.jar"
