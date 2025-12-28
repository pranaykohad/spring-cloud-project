@echo off
start cmd /k "java -jar "../../api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar" --spring.profiles.active=local"
start cmd /k "java -jar "../../user-service/target/user-service-0.0.2-SNAPSHOT.jar" --spring.profiles.active=local"
start cmd /k "java -jar "../../event-service/target/event-service-0.0.1-SNAPSHOT.jar" --spring.profiles.active=local"
start cmd /k "java -jar "../../notification-service/target/notification-service-0.0.1-SNAPSHOT.jar" --spring.profiles.active=local"