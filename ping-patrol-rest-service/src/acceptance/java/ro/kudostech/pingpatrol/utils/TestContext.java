package ro.kudostech.pingpatrol.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ro.kudostech.pingpatrol.api.client.Monitor;
import ro.kudostech.pingpatrol.api.client.RFC7807Problem;

@UtilityClass
public class TestContext {
  public ResponseEntity<Monitor> createMonitorResponse;
  public HttpStatusCode lastStatusCode;
  public RFC7807Problem lastProblem;
}
