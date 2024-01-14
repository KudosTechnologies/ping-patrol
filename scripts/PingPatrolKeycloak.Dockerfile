FROM quay.io/keycloak/keycloak:23.0.4

ENV KEYCLOAK_HOME /opt/keycloak
ADD scripts/config/keycloak/keycloak.conf ${KEYCLOAK_HOME}/conf/
ADD scripts/config/keycloak/quarkus.properties ${KEYCLOAK_HOME}/conf/
#ADD scripts/keycloak-custom-theme/themes/my-theme ${KEYCLOAK_HOME}/themes/my-theme
ADD scripts/keycloak-custom-theme/themes/ping-portal ${KEYCLOAK_HOME}/themes/ping-portal


USER root
RUN chmod -R g+r ${KEYCLOAK_HOME}/providers/
USER keycloak