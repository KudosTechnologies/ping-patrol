FROM quay.io/keycloak/keycloak:23.0.3

ENV KEYCLOAK_HOME /opt/keycloak
ADD scripts/config/keycloak/keycloak.conf ${KEYCLOAK_HOME}/conf/
ADD scripts/config/keycloak/quarkus.properties ${KEYCLOAK_HOME}/conf/

USER root
RUN chmod -R g+r ${KEYCLOAK_HOME}/providers/
USER keycloak