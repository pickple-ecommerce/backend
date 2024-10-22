FROM eclipse-temurin:17-jdk

ARG JAR_FILE=build/libs/*.jar

WORKDIR /app

COPY ${JAR_FILE} app.jar

EXPOSE ${EUREKA_SERVER_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]