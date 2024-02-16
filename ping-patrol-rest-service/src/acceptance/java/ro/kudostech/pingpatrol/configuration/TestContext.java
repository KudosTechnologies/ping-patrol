package ro.kudostech.pingpatrol.configuration;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.kudostech.pingpatrol.api.ApiClient;
import ro.kudostech.pingpatrol.api.client.MonitorsApi;
import ro.kudostech.pingpatrol.step.AuthorizationInterceptor;
import ro.kudostech.pingpatrol.utils.TokenUtils;

@Component
public class TestContext {

  private final TestRestTemplate testRestTemplate;

  public TestContext(TestRestTemplate testRestTemplate) {
    this.testRestTemplate = testRestTemplate;
  }

  private ApiClient initClient(String accessToken) {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add(new AuthorizationInterceptor(accessToken));

    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(testRestTemplate.getRootUri());
    return apiClient;
  }

  public final MonitorsApi getMonitorsApiWithAdminAccess() {
    return new MonitorsApi(initClient(TokenUtils.generateAdminJWT()));
  }
}
