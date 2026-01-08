FROM public.ecr.aws/amazoncorretto/amazoncorretto:21

WORKDIR /app

COPY target/user-service-0.0.2-SNAPSHOT.jar user-service-0.0.2-SNAPSHOT.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app/user-service-0.0.2-SNAPSHOT.jar"]