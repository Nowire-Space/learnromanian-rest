FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8081
WORKDIR /applications
COPY target/learnromanian-rest-0.0.1.jar /applications/learnromanian-rest.jar

ENTRYPOINT ["java","-jar", "learnromanian-rest.jar"]
