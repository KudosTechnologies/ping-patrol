import Keycloak from "keycloak-js";

const keycloakConfig = {
    url: 'http://localhost:9080',
    realm: 'pingpatrol',
    clientId: 'pingpatrol-webapp',
};

/**
 * `initOptions` is an object that holds the configuration options for Keycloak initialization.
 *
 * @property {string} onLoad - Determines how to handle the Keycloak initialization.
 * 'check-sso' will only authenticate the client if the user is already logged in, 
 * if the user is not logged in, the browser will be redirected back to the application 
 * (it won't show the login page).
 *
 * @property {boolean} checkLoginIframe - If set to false, the Keycloak will not check 
 * if the user is logged in using a third-party cookies check. This is useful in scenarios 
 * where third-party cookies are blocked by the browser.
 */

const initOptions = {
    onLoad: 'check-sso',
    checkLoginIframe: false
};

const keycloak = new Keycloak(keycloakConfig);

export default keycloak;