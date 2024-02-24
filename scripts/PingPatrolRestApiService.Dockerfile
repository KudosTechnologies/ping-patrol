FROM eclipse-temurin:21 AS builder

COPY ping-patrol-rest-service ping-patrol-rest-service
WORKDIR ping-patrol-rest-service
RUN ./gradlew clean build
RUN mv build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract


FROM eclipse-temurin:21-jre-jammy
WORKDIR application
COPY --from=builder ping-patrol-rest-service/dependencies/ ./
COPY --from=builder ping-patrol-rest-service/spring-boot-loader/ ./
COPY --from=builder ping-patrol-rest-service/snapshot-dependencies/ ./
COPY --from=builder ping-patrol-rest-service/application/ ./

EXPOSE 8080

ENTRYPOINT ["java" , "org.springframework.boot.loader.launch.JarLauncher"]
