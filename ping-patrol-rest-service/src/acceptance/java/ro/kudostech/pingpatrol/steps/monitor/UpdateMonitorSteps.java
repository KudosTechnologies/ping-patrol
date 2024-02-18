package ro.kudostech.pingpatrol.steps.monitor;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import ro.kudostech.pingpatrol.AutowiredIgnoreWarning;
import ro.kudostech.pingpatrol.PingPatrolApi;
import ro.kudostech.pingpatrol.api.client.Monitor;
import ro.kudostech.pingpatrol.api.client.MonitorPatchOperation;
import ro.kudostech.pingpatrol.api.client.MonitorType;
import ro.kudostech.pingpatrol.api.client.PatchOperation;
import ro.kudostech.pingpatrol.api.client.UpdateMonitorRequest;
import ro.kudostech.pingpatrol.utils.JsonUtils;
import ro.kudostech.pingpatrol.utils.TestContext;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class UpdateMonitorSteps {

  @AutowiredIgnoreWarning private PingPatrolApi pingPatrolApi;

  @When(
      "update monitor with name {string} and type {string} and url {string} and monitoring interval {int} and monitor timeout {int}")
  public synchronized void updateMonitorWithNameAndTypeAndUrlAndMonitoringIntervalAndMonitorTimeout(
      String name, String type, String url, int monitoringInterval, int monitorTimeout)
      throws JsonProcessingException {
    UpdateMonitorRequest updateMonitorRequest =
        UpdateMonitorRequest.builder()
            .name(name)
            .type(MonitorType.valueOf(type))
            .url(url)
            .monitoringInterval(monitoringInterval)
            .monitorTimeout(monitorTimeout)
            .build();

    try {

      UUID monitorId = Objects.requireNonNull(TestContext.createMonitorResponse.getBody()).getId();
      TestContext.lastStatusCode =
          pingPatrolApi
              .getMonitorsApiWithAdminAccess()
              .updateMonitorWithHttpInfo(monitorId, updateMonitorRequest)
              .getStatusCode();
    } catch (HttpClientErrorException e) {
      log.info("Could not update monitor with message: {}", e.getMessage());
      TestContext.lastProblem = JsonUtils.parseProblem(e.getResponseBodyAsString());
      TestContext.lastStatusCode = e.getStatusCode();
    }
  }
}
