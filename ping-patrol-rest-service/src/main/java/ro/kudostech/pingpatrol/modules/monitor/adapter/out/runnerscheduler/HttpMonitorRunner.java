package ro.kudostech.pingpatrol.modules.monitor.adapter.out.runnerscheduler;

import lombok.extern.slf4j.Slf4j;
import ro.kudostech.pingpatrol.api.server.model.MonitorRunStatus;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRunDbo;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRunRepository;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

@Slf4j
public class HttpMonitorRunner implements Runnable {

  private final String monitorId;

  private final String url;
  private final int timeout;

  private final MonitorRunRepository monitorRunRepository;

  private final HttpClient httpClient;

  public HttpMonitorRunner(
      String monitorId, String url, int timeout, MonitorRunRepository monitorRunRepository) {
    this.monitorId = monitorId;
    this.url = url;
    this.timeout = timeout;
    this.httpClient = HttpClient.newHttpClient();
    this.monitorRunRepository = monitorRunRepository;
  }

  @Override
  public void run() {
    Instant startedAt = Instant.now();
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(timeout))
            .GET()
            .build();
    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() >= 200 && response.statusCode() < 300) {
        logMonitorRun(monitorId, MonitorRunStatus.REACHABLE.name(), startedAt, null);
      } else {
        logMonitorRun(
            monitorId,
            MonitorRunStatus.UNREACHABLE.name(),
            startedAt,
            "HTTP status code: " + response.statusCode());
      }
    } catch (InterruptedException e) {
      log.error("Interrupted while running monitor for url: {}", url, e);
      Thread.currentThread().interrupt();
    } catch (ConnectException e) {
      logMonitorRun(
          monitorId, MonitorRunStatus.UNREACHABLE.name(), startedAt, "Connection refused");
    } catch (Exception e) {
      logMonitorRun(monitorId, MonitorRunStatus.UNREACHABLE.name(), startedAt, e.getMessage());
    }
  }

  private void logMonitorRun(
      String monitorId, String status, Instant startedAt, String errorDetails) {
    log.info(
        "[MONITOR RUN] - Details monitorId: {}, status: {}, startedAt: {}, errorDetails: {}",
        monitorId,
        status,
        startedAt,
        errorDetails);
    MonitorRunDbo monitorRunDbo =
        MonitorRunDbo.builder()
            .monitorId(monitorId)
            .status(status)
            .duration(Instant.now().toEpochMilli() - startedAt.toEpochMilli())
            .startedAt(startedAt)
            .errorDetails(errorDetails)
            .build();
    this.monitorRunRepository.save(monitorRunDbo);
  }
}
