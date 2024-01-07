package ro.kudostech.pingpatrol.configuration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import org.jose4j.jwk.JsonWebKeySet;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ro.kudostech.pingpatrol.Application;
import ro.kudostech.pingpatrol.utils.TokenUtils;

@CucumberContextConfiguration
@ActiveProfiles("acceptancetest")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
public class WireMockConfigurations {

  private WireMockConfigurations() {}

  private static WireMockServer wireMockServer;
  public static String keycloakBaseUrl;

  @BeforeAll
  public static void setUp() {
    WireMockConfiguration config = WireMockConfiguration.wireMockConfig().port(0);
    wireMockServer = new WireMockServer(config);
    wireMockServer.start();
    WireMock.configureFor("localhost", wireMockServer.port());

    keycloakBaseUrl = "http://localhost:" + wireMockServer.port();
    System.setProperty("wiremock.server.port", String.valueOf(wireMockServer.port()));

    stubFor(
        WireMock.get(
                urlEqualTo(
                    String.format("/realms/%s/protocol/openid-connect/certs", TokenUtils.KEYCLOAK_REALM)))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(new JsonWebKeySet(TokenUtils.generateRsaJsonWebKey()).toJson())));
  }

  @AfterAll
  public static void afterAll() {
    wireMockServer.stop();
  }
}
