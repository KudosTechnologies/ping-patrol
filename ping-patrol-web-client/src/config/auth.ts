import Keycloak from "keycloak-js";

const keycloakConfig = {
    url: 'http://localhost:9080',
    realm: 'pingpatrol',
    clientId: 'pingpatrol-webapp',
};

const keycloak = new Keycloak(keycloakConfig);

export default keycloak;