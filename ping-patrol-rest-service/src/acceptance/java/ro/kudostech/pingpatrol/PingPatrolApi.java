package ro.kudostech.pingpatrol;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.kudostech.pingpatrol.api.ApiClient;
import ro.kudostech.pingpatrol.api.client.MonitorsApi;
import ro.kudostech.pingpatrol.utils.TokenUtils;

@Component
public class PingPatrolApi {

  private final TestRestTemplate testRestTemplate;

  public PingPatrolApi(TestRestTemplate testRestTemplate) {
    this.testRestTemplate = testRestTemplate;
  }

  public final MonitorsApi getMonitorsApiWithAdminAccess() {
    return new MonitorsApi(initClient(TokenUtils.generateAdminJWT()));
  }

  private ApiClient initClient(String accessToken) {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    restTemplate.getInterceptors().add(new AuthorizationInterceptor(accessToken));

    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(testRestTemplate.getRootUri());
    return apiClient;
  }
}
