FROM maven:3.9.6-eclipse-temurin-17-alpine AS maven_builder

COPY kudconnect-service-api kudconnect-service-api
COPY kudconnect-service kudconnect-service

WORKDIR kudconnect-service-api
RUN ./gradlew publishToMavenLocal

WORKDIR ../kudconnect-service
RUN ./gradlew clean build

FROM eclipse-temurin:21.0.1_12-jre-alpine

COPY --from=maven_builder kudconnect-service/build/libs/*.jar kudconnect-service.jar
ADD https://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/1.43.0/elastic-apm-agent-1.43.0.jar elastic-apm-agent.jar

ENV APM_AGENT_OPTS="-javaagent:elastic-apm-agent.jar -Delastic.apm.service_name=kudconnect-service -Delastic.apm.server_url=http://10.50.8.11:8200 -Delastic.apm.environment=dev -Delastic.apm.application_packages=ro.kudostech -Delastic.apm.disable_bootstrap_checks=true"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${APM_AGENT_OPTS} -jar /kudconnect-service.jar --spring.profiles.active=${ACTIVE_PROFILE}"]