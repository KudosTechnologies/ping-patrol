package ro.kudostech.pingpatrol;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ro.kudostech.pingpatrol.api.client.CreateMonitorRequest;
import ro.kudostech.pingpatrol.api.client.MonitorType;
import ro.kudostech.pingpatrol.configuration.TestContext;
import static org.assertj.core.api.Assertions.assertThat;

public class CrudMonitorSteps {

  @AutowiredIgnoreWarning private TestContext testContext;

  @Given("the system has no existing monitor with name {string}")
  public void systemHasNoMonitorWithEmail(String email) {
    assertThat(
            testContext.getMonitorsApiWithAdminAccess().getAllMonitors().stream()
                .anyMatch(
                    monitor -> {
                      assert monitor.getName() != null;
                      return monitor.getName().equals(email);
                    }))
        .isFalse();
  }

  @When("I create a monitor with name {string} of type {string} and url {string}")
  public void createMonitor(String name, String type, String url) {
    CreateMonitorRequest createMonitorRequest =
        CreateMonitorRequest.builder()
            .name(name)
            .type(MonitorType.valueOf(type))
            .url(url)
            .monitoringInterval(5)
            .monitorTimeout(5)
            .build();
    testContext.getMonitorsApiWithAdminAccess().createMonitor(createMonitorRequest);
  }

  @Then("the system should have a monitor with name {string}")
  public void systemShouldHaveMonitor(String name) {
    assertThat(
            testContext.getMonitorsApiWithAdminAccess().getAllMonitors().stream()
                .anyMatch(
                    monitor -> {
                      assert monitor.getName() != null;
                      return monitor.getName().equals(name);
                    }))
        .isTrue();
  }
}
