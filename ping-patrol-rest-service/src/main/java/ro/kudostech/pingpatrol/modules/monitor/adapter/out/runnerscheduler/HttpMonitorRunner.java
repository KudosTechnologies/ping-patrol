package ro.kudostech.pingpatrol.modules.monitor.adapter.out.runnerscheduler;

import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
public class HttpMonitorRunner implements Runnable {

  private final String url;
  private final int timeout;

  private final HttpClient httpClient;

  public HttpMonitorRunner(String url, int timeout) {
    this.url = url;
    this.timeout = timeout;
    this.httpClient = HttpClient.newHttpClient();
  }

  @Override
  public void run() {
    log.info("Running monitor for url: {}", url);
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(timeout))
            .GET()
            .build();
    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      log.info("Response status code: {}", response.statusCode());
      log.info("Response body: {}", response.body());
    } catch (InterruptedException e) {
      log.error("Interrupted while running monitor for url: {}", url, e);
      Thread.currentThread().interrupt();
    } catch (ConnectException e) {
      log.error("Connection error while running monitor for url: {}", url, e);
    } catch (Exception e) {
      log.error("Error while running monitor for url: {}", url, e);
    }
  }
}
