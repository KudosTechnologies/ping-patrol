package ro.kudostech.pingpatrol.modules.monitor.adapter.in.rest;

import org.springframework.http.ResponseEntity;
import ro.kudostech.pingpatrol.api.server.MonitorsApi;
import ro.kudostech.pingpatrol.api.server.model.CreateMonitorRequest;
import ro.kudostech.pingpatrol.api.server.model.Monitor;
import ro.kudostech.pingpatrol.api.server.model.MonitorEvent;
import ro.kudostech.pingpatrol.api.server.model.MonitorProbes;
import ro.kudostech.pingpatrol.api.server.model.UpdateMonitorRequest;

import java.util.List;
import java.util.UUID;

public class MonitorController implements MonitorsApi {
  @Override
  public ResponseEntity<Monitor> createMonitor(CreateMonitorRequest createMonitorRequest) {
    return null;
  }

  @Override
  public ResponseEntity<Void> deleteMonitorById(UUID monitorId) {
    return null;
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
    return null;
  }

  @Override
  public ResponseEntity<Monitor> getMonitorById(UUID monitorId) {
    return null;
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
