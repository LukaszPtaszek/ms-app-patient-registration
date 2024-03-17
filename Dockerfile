FROM openjdk:17-alpine
ARG JAR_NAME="ms-app-patient-registration-G01R00C00-SNAPSHOT.jar"
RUN apk --no-cache add curl
COPY target/${JAR_NAME} app.jar
CMD ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]
