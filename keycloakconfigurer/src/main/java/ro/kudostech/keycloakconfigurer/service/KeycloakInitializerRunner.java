package ro.kudostech.keycloakconfigurer.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.IdentityProviderRepresentation;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RequiredActionProviderRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakInitializerRunner implements CommandLineRunner {

  @Value("${keycloak.base-url}")
  public String keycloakBaseUrl;

  @Value("${GOOGLE_CLIENT_ID}")
  public String googleClientId;

  @Value("${GOOGLE_CLIENT_SECRET}")
  public String googleClientSecret;

  private static final String REALM_NAME = "pingpatrol";
  private static final String CLIENT_ID = "pingpatrol-webapp";
  private static final String LOGIN_THEME = "ping-portal";

  private static final List<String> REDIRECT_URL_LIST =
      List.of(
          "http://localhost:81/*",
          "http://localhost:3000/*",
          "http://pingpatrol.local/*",
          "https://pingpatrol.dev.kudostech.ro/*");
  private static final String USER_ID_CLAIM = "user_id";
  private static final List<UserPass> USER_LIST =
      Arrays.asList(new UserPass("admin@test.com", "admin"), new UserPass("user@test.com", "user"));
  private static final String ROLE_USER = "user";
  private static final String ROLE_ADMIN = "admin";

  record UserPass(String id, String password, String email) {
    public UserPass(String email, String password) {
      this(null, password, email);
    }
  }

  private final Keycloak keycloakAdmin;

  @Override
  public void run(String... args) {
    log.info("Initializing '{}' realm in Keycloak ...", REALM_NAME);

    cleanUpRealm();

    // Realm
    RealmRepresentation realmRepresentation = new RealmRepresentation();
    realmRepresentation.setRealm(REALM_NAME);
    realmRepresentation.setEnabled(true);
    realmRepresentation.setRegistrationAllowed(true);
    realmRepresentation.setLoginWithEmailAllowed(true);
    realmRepresentation.setRegistrationEmailAsUsername(true);

    // Set Login Theme
    realmRepresentation.setLoginTheme(LOGIN_THEME);

    // Additional IdPs
    configureGoogleIdentityProvider(realmRepresentation);

    // Ream Clients
    configureRealmWebClient(realmRepresentation);
    configureRealmPrivateClient(realmRepresentation);

    // DefaultUsers
    addDefaultUsersToRealmRepresentation(realmRepresentation);

    // Create Realm
    keycloakAdmin.realms().create(realmRepresentation);

    disableVerifyProfileAction();

    // Testing
    testConfiguration();
  }

  private void disableVerifyProfileAction() {
    RealmResource realmResource = keycloakAdmin.realm(REALM_NAME);
    RequiredActionProviderRepresentation verifyProfileAction = realmResource.flows().getRequiredAction("VERIFY_PROFILE");
    verifyProfileAction.setEnabled(false);
    realmResource.flows().updateRequiredAction("VERIFY_PROFILE", verifyProfileAction);
  }

  private Map<String, List<String>> getClientRoles(UserPass userPass) {
    List<String> roles = new ArrayList<>();
    roles.add(ROLE_USER);
    if ("admin@test.com".equals(userPass.email())) {
      roles.add(ROLE_ADMIN);
    }
    return Map.of(CLIENT_ID, roles);
  }

  private void cleanUpRealm() {
    Optional<RealmRepresentation> representationOptional =
        keycloakAdmin.realms().findAll().stream()
            .filter(r -> r.getRealm().equals(REALM_NAME))
            .findAny();
    if (representationOptional.isPresent()) {
      log.info("Removing already pre-configured '{}' realm", REALM_NAME);
      keycloakAdmin.realm(REALM_NAME).remove();
    }
  }

  private void configureGoogleIdentityProvider(RealmRepresentation realmRepresentation) {
    if (googleClientId == null || googleClientSecret == null) {
      log.warn("Google ClientId and/or ClientSecret not set. Skipping Google Identity Provider");
      return;
    }

    IdentityProviderRepresentation googleIdentityProvider = new IdentityProviderRepresentation();
    googleIdentityProvider.setAlias("google");
    googleIdentityProvider.setDisplayName("Google");
    googleIdentityProvider.setProviderId("google");
    googleIdentityProvider.setEnabled(true);
    googleIdentityProvider.setStoreToken(false);
    googleIdentityProvider.setLinkOnly(false);
    googleIdentityProvider.setFirstBrokerLoginFlowAlias("first broker login");

    Map<String, String> googleConfig = new HashMap<>();
    googleConfig.put("clientId", googleClientId);
    googleConfig.put("clientSecret", googleClientSecret);
    googleIdentityProvider.setConfig(googleConfig);
    realmRepresentation.addIdentityProvider(googleIdentityProvider);
  }

  private void configureRealmWebClient(RealmRepresentation realmRepresentation) {

    // Configure custom provider
    ProtocolMapperRepresentation mapper = new ProtocolMapperRepresentation();
    mapper.setName("additional-claims-mapper");
    mapper.setProtocol("openid-connect");
    mapper.setProtocolMapper("oidc-usermodel-attribute-mapper");
    //    mapper.setProtocolMapper("custom-protocol-mapper");

    Map<String, String> config = new HashMap<>();
    config.put("user.attribute", USER_ID_CLAIM);
    config.put("access.token.claim", "true");
    config.put("claim.name", USER_ID_CLAIM);
    config.put("jsonType.label", "String");
    mapper.setConfig(config);

    ClientRepresentation clientRepresentation = new ClientRepresentation();
    clientRepresentation.setClientId(CLIENT_ID);
    clientRepresentation.setDirectAccessGrantsEnabled(true);
    clientRepresentation.setPublicClient(true);
    clientRepresentation.setRedirectUris(REDIRECT_URL_LIST);
    clientRepresentation.setDefaultRoles(new String[] {ROLE_USER});
    clientRepresentation.setProtocolMappers(List.of(mapper));

    addClientToRealmRepresentation(realmRepresentation, clientRepresentation);
  }

  private void configureRealmPrivateClient(RealmRepresentation realmRepresentation) {
    ClientRepresentation confidentialClientRepresentation = new ClientRepresentation();
    confidentialClientRepresentation.setClientId("helpdesk-app");
    confidentialClientRepresentation.setSecret("helpdesk-app-dummy-secret"); // set a secret here
    confidentialClientRepresentation.setServiceAccountsEnabled(true);
    confidentialClientRepresentation.setDirectAccessGrantsEnabled(true);
    confidentialClientRepresentation.setPublicClient(false); // it is not a public client
    confidentialClientRepresentation.setRedirectUris(REDIRECT_URL_LIST);

    addClientToRealmRepresentation(realmRepresentation, confidentialClientRepresentation);
  }

  private void addClientToRealmRepresentation(
      RealmRepresentation realmRepresentation, ClientRepresentation clientRepresentation) {
    List<ClientRepresentation> clients = realmRepresentation.getClients();
    if (CollectionUtils.isEmpty(clients)) {
      clients = new ArrayList<>();
      clients.add(clientRepresentation);
      realmRepresentation.setClients(clients);
    } else {
      realmRepresentation.getClients().add(clientRepresentation);
    }
  }

  private void addDefaultUsersToRealmRepresentation(RealmRepresentation realmRepresentation) {
    List<UserRepresentation> userRepresentations =
        USER_LIST.stream()
            .map(
                userPass -> {
                  // User Credentials
                  CredentialRepresentation credentialRepresentation =
                      new CredentialRepresentation();
                  credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                  credentialRepresentation.setValue(userPass.password());

                  // User
                  UserRepresentation userRepresentation = new UserRepresentation();
                  userRepresentation.setUsername(userPass.email());
                  userRepresentation.setEmail(userPass.email());
                  userRepresentation.setEnabled(true);
                  userRepresentation.setCredentials(List.of(credentialRepresentation));
                  userRepresentation.setClientRoles(getClientRoles(userPass));
                  return userRepresentation;
                })
            .toList();
    realmRepresentation.setUsers(userRepresentations);
  }

  private void testConfiguration() {
    UserPass admin = USER_LIST.get(0);
    log.info("Testing getting token for '{}' ...", admin.email());

    Keycloak pingPatrolWebApp =
        KeycloakBuilder.builder()
            .serverUrl(keycloakBaseUrl)
            .realm(REALM_NAME)
            .username(admin.email())
            .password(admin.password())
            .clientId(CLIENT_ID)
            .build();

    log.info(
        "'{}' token: {}", admin.email(), pingPatrolWebApp.tokenManager().grantToken().getToken());
    log.info("'{}' initialization completed successfully!", REALM_NAME);
  }
}
