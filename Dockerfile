FROM openjdk:16-jdk-alpine AS builder

WORKDIR /app
COPY gradle/ gradle/
COPY src/main/ src/main/
COPY build.gradle.kts gradle.properties gradlew settings.gradle.kts ./
COPY build/libs/*.jar /app/build/libs/app.jar
COPY build/resources/main /app/build/resources/main/
RUN chmod +x gradlew
RUN if [ ! -f "/app/build/libs/app.jar" ]; then ./gradlew shadowJar; fi

FROM openjdk:16-jdk-alpine
COPY --from=builder /app/build/libs/*.jar /opt/program/app.jar
COPY --from=builder /app/build/resources/main/ /opt/program/src/main/resources/

EXPOSE 6543
WORKDIR /app
ENTRYPOINT [ "java", "-Xmx2G", "-jar", "/opt/program/app.jar" ]
