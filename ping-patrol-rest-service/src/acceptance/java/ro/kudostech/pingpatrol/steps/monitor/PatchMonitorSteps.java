package ro.kudostech.pingpatrol.steps.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import ro.kudostech.pingpatrol.AutowiredIgnoreWarning;
import ro.kudostech.pingpatrol.PingPatrolApi;
import ro.kudostech.pingpatrol.api.client.MonitorPatchOperation;
import ro.kudostech.pingpatrol.api.client.PatchOperation;
import ro.kudostech.pingpatrol.utils.JsonUtils;
import ro.kudostech.pingpatrol.utils.TestContext;

@Slf4j
public class PatchMonitorSteps {

  @AutowiredIgnoreWarning private PingPatrolApi pingPatrolApi;

  @When("patch monitor with name {string} and type {string}")
  public synchronized void patchMonitorWithNameAndType(String name, String type)
      throws JsonProcessingException {
    MonitorPatchOperation namePatchOperation =
        MonitorPatchOperation.builder()
            .path(MonitorPatchOperation.PathEnum.NAME)
            .op(PatchOperation.REPLACE)
            .value(name)
            .build();
    MonitorPatchOperation typePatchOperation =
        MonitorPatchOperation.builder()
            .path(MonitorPatchOperation.PathEnum.TYPE)
            .op(PatchOperation.REPLACE)
            .value(type)
            .build();

    try {
      UUID monitorId = Objects.requireNonNull(TestContext.createMonitorResponse.getBody()).getId();
      TestContext.lastStatusCode =
          pingPatrolApi
              .getMonitorsApiWithAdminAccess()
              .patchMonitorByIdWithHttpInfo(
                  monitorId, List.of(namePatchOperation, typePatchOperation))
              .getStatusCode();
    } catch (HttpClientErrorException e) {
      TestContext.lastProblem = JsonUtils.parseProblem(e.getResponseBodyAsString());
    }
  }
}
