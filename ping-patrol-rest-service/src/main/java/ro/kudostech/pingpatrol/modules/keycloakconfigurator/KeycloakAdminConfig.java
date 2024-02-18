package ro.kudostech.pingpatrol.modules.keycloakconfigurator;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//@Configuration
@Profile("!acceptancetest")
public class KeycloakAdminConfig {

  @Value("${keycloak.base-url}")
  public String keycloakBaseUrl;

  @Value("${keycloak.admin-user:admin}")
  public String keycloakAdminUser;

  @Value("${keycloak.admin-password:admin}")
  public String keycloakAdminPassword;

  @Bean
  public Keycloak keycloakAdmin() {
    return KeycloakBuilder.builder()
        .serverUrl(keycloakBaseUrl)
        .realm("master")
        .username(keycloakAdminUser)
        .password(keycloakAdminPassword)
        .clientId("admin-cli")
        .build();
  }
}
