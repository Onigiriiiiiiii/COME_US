FROM eclipse-temurin:25-jre

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=${SPRING_ACTIVE_PROFILE}","app.jar"]