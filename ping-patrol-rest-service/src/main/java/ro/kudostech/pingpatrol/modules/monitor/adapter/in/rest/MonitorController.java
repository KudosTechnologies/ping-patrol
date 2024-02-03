package ro.kudostech.pingpatrol.modules.monitor.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import ro.kudostech.pingpatrol.api.server.MonitorsApi;
import ro.kudostech.pingpatrol.api.server.model.CreateMonitorRequest;
import ro.kudostech.pingpatrol.api.server.model.Monitor;
import ro.kudostech.pingpatrol.api.server.model.MonitorEvent;
import ro.kudostech.pingpatrol.api.server.model.MonitorProbes;
import ro.kudostech.pingpatrol.api.server.model.UpdateMonitorRequest;
import org.springframework.web.bind.annotation.RestController;
import ro.kudostech.pingpatrol.modules.monitor.ports.MonitorService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MonitorController implements MonitorsApi {

  private final MonitorService monitorService;

  @Override
  public ResponseEntity<Monitor> createMonitor(CreateMonitorRequest createMonitorRequest) {

    Monitor monitor = monitorService.createMonitor(createMonitorRequest);
    URI location = URI.create("/monitors/" + monitor.getId());
    return ResponseEntity.created(location).body(monitor);
  }

  @Override
  public ResponseEntity<Void> deleteMonitorById(UUID monitorId) {
    monitorService.deleteMonitorById(monitorId.toString());
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<List<MonitorEvent>> getAllMonitorEvents(UUID monitorId) {
    return null;
  }

  @Override
  public ResponseEntity<List<MonitorProbes>> getAllMonitorProbes(UUID monitorId) {
    return null;
  }

  @Override
  public ResponseEntity<List<Monitor>> getAllMonitors() {
    return ResponseEntity.ok(monitorService.getAllMonitors());
  }

  @Override
  public ResponseEntity<Monitor> getMonitorById(UUID monitorId) {
    return ResponseEntity.ok(monitorService.getMonitorById(monitorId.toString()));
  }

  @Override
  public ResponseEntity<Monitor> pauseMonitorById(UUID monitorId) {
    return null;
  }

  @Override
  public ResponseEntity<Monitor> resetMonitorById(UUID monitorId) {
    return null;
  }

  @Override
  public ResponseEntity<Monitor> resumeMonitorById(UUID monitorId) {
    return null;
  }

  @Override
  public ResponseEntity<Monitor> updateMonitor(UpdateMonitorRequest updateMonitorRequest) {
    return null;
  }
}
