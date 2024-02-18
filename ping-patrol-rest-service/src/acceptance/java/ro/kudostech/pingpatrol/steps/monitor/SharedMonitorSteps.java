package ro.kudostech.pingpatrol.steps.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import ro.kudostech.pingpatrol.AutowiredIgnoreWarning;
import ro.kudostech.pingpatrol.PingPatrolApi;
import ro.kudostech.pingpatrol.api.client.CreateMonitorRequest;
import ro.kudostech.pingpatrol.api.client.Monitor;
import ro.kudostech.pingpatrol.api.client.MonitorType;
import ro.kudostech.pingpatrol.utils.JsonUtils;
import ro.kudostech.pingpatrol.utils.TestContext;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class SharedMonitorSteps {

  private static final String MONITOR_DEFAULT_URL = "https://www.google.com";
  private static final MonitorType MONITOR_DEFAULT_TYPE = MonitorType.HTTPS;
  private static final int MONITOR_DEFAULT_MONITORING_INTERVAL = 60;
  private static final int MONITOR_DEFAULT_MONITOR_TIMEOUT = 100;

  @AutowiredIgnoreWarning private PingPatrolApi pingPatrolApi;

  @Given("delete all existing monitors")
  public void deleteAllExistingMonitors() {
    pingPatrolApi
        .getMonitorsApiWithAdminAccess()
        .getAllMonitors()
        .forEach(
            monitor ->
                pingPatrolApi.getMonitorsApiWithAdminAccess().deleteMonitorById(monitor.getId()));
  }

  @Given("create monitor with name {string} and default values")
  public synchronized void createMonitorWithNameAndDefaultValues(String name)
      throws JsonProcessingException {
    CreateMonitorRequest createMonitorRequest =
        CreateMonitorRequest.builder()
            .name(name)
            .type(MONITOR_DEFAULT_TYPE)
            .url(MONITOR_DEFAULT_URL)
            .monitoringInterval(MONITOR_DEFAULT_MONITORING_INTERVAL)
            .monitorTimeout(MONITOR_DEFAULT_MONITOR_TIMEOUT)
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

  @Given("the system has no existing monitor with name {string}")
  public void systemHasNoMonitorWithEmail(String email) {
    assertThat(
            pingPatrolApi.getMonitorsApiWithAdminAccess().getAllMonitors().stream()
                .anyMatch(
                    monitor -> {
                      assert monitor.getName() != null;
                      return monitor.getName().equals(email);
                    }))
        .isFalse();
  }

  @Then("statusCode is {string}")
  public void monitorIsNotCreated(String statusCode) {
    assertThat(TestContext.lastStatusCode)
        .isEqualTo(HttpStatusCode.valueOf(Integer.parseInt(statusCode)));
  }

  @And(
      "contains error message {string} with violation field {string} and violation message {string}")
  public void containsErrorMessageWithViolationFieldAndViolationMessage(
      String errorMessage, String violationField, String violationMessage) {
    assertThat(TestContext.lastProblem.getDetail()).isEqualTo(errorMessage);
    assertThat(
            TestContext.lastProblem.getViolations() != null
                ? TestContext.lastProblem.getViolations().getFirst().getField()
                : null)
        .isEqualTo(violationField);
    assertThat(TestContext.lastProblem.getViolations().getFirst().getMessage())
        .isEqualTo(violationMessage);
  }

  @And(
      "updated monitor should have name {string} and type {string} and url {string} and monitoring interval {int} and monitor timeout {int}")
  public void updatedMonitorShouldHaveNameAndTypeAndUrlAndMonitoringIntervalAndMonitorTimeout(
      String name, String type, String url, int monitoringInterval, int monitorTimeout) {
    UUID monitorId = Objects.requireNonNull(TestContext.createMonitorResponse.getBody()).getId();
    Monitor updatedMonitor =
        pingPatrolApi.getMonitorsApiWithAdminAccess().getMonitorById(monitorId);
    assertThat(updatedMonitor.getName()).isEqualTo(name);
    assertThat(updatedMonitor.getType()).isEqualTo(MonitorType.valueOf(type));
    assertThat(updatedMonitor.getUrl()).isEqualTo(url);
    assertThat(updatedMonitor.getMonitoringInterval()).isEqualTo(monitoringInterval);
    assertThat(updatedMonitor.getMonitorTimeout()).isEqualTo(monitorTimeout);
  }
}
