FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=build/libs/*.jar

WORKDIR /app

COPY ${JAR_FILE} app.jar

EXPOSE ${PAYMENT_SERVER_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]