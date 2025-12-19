FROM public.ecr.aws/amazoncorretto/amazoncorretto:17

WORKDIR /app

COPY target/api-gateway-0.0.1-SNAPSHOT.jar api-gateway-0.0.1-SNAPSHOT.jar

EXPOSE 8083

ENTRYPOINT ["java","-jar","/app/api-gateway-0.0.1-SNAPSHOT.jar"]