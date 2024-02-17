package ro.kudostech.pingpatrol.steps.monitor;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import ro.kudostech.pingpatrol.AutowiredIgnoreWarning;
import ro.kudostech.pingpatrol.PingPatrolApi;
import ro.kudostech.pingpatrol.api.client.CreateMonitorRequest;
import ro.kudostech.pingpatrol.api.client.MonitorType;
import ro.kudostech.pingpatrol.utils.JsonUtils;
import ro.kudostech.pingpatrol.utils.TestContext;

@Slf4j
public class CreateMonitorSteps {

  @AutowiredIgnoreWarning private PingPatrolApi pingPatrolApi;

  @When("I create a monitor with name {string} of type {string} and url {string}")
  public synchronized void createMonitor(String name, String type, String url) throws JsonProcessingException {
    CreateMonitorRequest createMonitorRequest =
        CreateMonitorRequest.builder()
            .name(name)
            .type(MonitorType.valueOf(type))
            .url(url)
            .monitoringInterval(5)
            .monitorTimeout(5)
            .build();
    try {
      TestContext.createMonitorResponse =
          pingPatrolApi
              .getMonitorsApiWithAdminAccess()
              .createMonitorWithHttpInfo(createMonitorRequest);
      TestContext.lastStatusCode = TestContext.createMonitorResponse.getStatusCode();
    } catch (HttpClientErrorException e) {
      log.info("Could not create monitor with message: {}", e.getResponseBodyAsString());
      TestContext.lastProblem = JsonUtils.parseProblem(e.getResponseBodyAsString());
      TestContext.lastStatusCode = e.getStatusCode();
    }
  }

  @Then("the system should have a monitor with name {string}")
  public void systemShouldHaveMonitor(String name) {
    assertThat(
            pingPatrolApi.getMonitorsApiWithAdminAccess().getAllMonitors().stream()
                .anyMatch(
                    monitor -> {
                      assert monitor.getName() != null;
                      return monitor.getName().equals(name);
                    }))
        .isTrue();
  }
}
