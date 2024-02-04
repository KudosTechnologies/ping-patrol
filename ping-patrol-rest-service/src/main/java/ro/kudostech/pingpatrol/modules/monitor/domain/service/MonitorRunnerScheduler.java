package ro.kudostech.pingpatrol.modules.monitor.domain.service;

import ro.kudostech.pingpatrol.api.server.model.Monitor;

public interface MonitorRunnerScheduler {

    void scheduleMonitorRunner(Monitor monitor);

    void pauseMonitorRunner(String monitorId);
}
