package ro.kudostech.pingpatrol.modules.monitor.ports;

import ro.kudostech.pingpatrol.api.server.model.CreateMonitorRequest;
import ro.kudostech.pingpatrol.api.server.model.Monitor;

import java.util.List;

public interface MonitorService {
    Monitor createMonitor(CreateMonitorRequest createMonitorRequest);

    Monitor getMonitorById(String monitorId);

    List<Monitor> getAllMonitors();

    void deleteMonitorById(String monitorId);
}
