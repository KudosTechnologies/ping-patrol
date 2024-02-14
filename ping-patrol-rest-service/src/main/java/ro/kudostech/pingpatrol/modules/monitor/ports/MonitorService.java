package ro.kudostech.pingpatrol.modules.monitor.ports;

import ro.kudostech.pingpatrol.api.server.model.CreateMonitorRequest;
import ro.kudostech.pingpatrol.api.server.model.Monitor;
import ro.kudostech.pingpatrol.api.server.model.MonitorPatchOperation;
import ro.kudostech.pingpatrol.api.server.model.MonitorRun;
import ro.kudostech.pingpatrol.api.server.model.UpdateMonitorRequest;

import java.util.List;

public interface MonitorService {
    Monitor createMonitor(CreateMonitorRequest createMonitorRequest);

    Monitor getMonitorById(String monitorId);

    List<Monitor> getAllMonitors();

    void deleteMonitorById(String monitorId);

    Monitor updateMonitor(String monitorId, UpdateMonitorRequest createMonitorRequest);

    Monitor resumeMonitorById(String monitorId);

    Monitor pauseMonitorById(String monitorId);

    List<MonitorRun> getAllMonitorRuns(String monitorId);

    void deleteMonitorRuns(String monitorId);

    Monitor patchMonitorById(String string, List<MonitorPatchOperation> monitorPatchOperation);
}
