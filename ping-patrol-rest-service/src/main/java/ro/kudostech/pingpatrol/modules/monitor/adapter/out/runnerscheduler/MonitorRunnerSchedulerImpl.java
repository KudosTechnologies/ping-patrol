package ro.kudostech.pingpatrol.modules.monitor.adapter.out.runnerscheduler;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import ro.kudostech.pingpatrol.api.server.model.Monitor;
import ro.kudostech.pingpatrol.modules.monitor.domain.service.MonitorRunnerScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
public class MonitorRunnerSchedulerImpl implements MonitorRunnerScheduler {

    private final TaskScheduler taskScheduler;
    private Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();


    @Override
    public void scheduleMonitorRunner(Monitor monitor) {

    }

    @Override
    public void pauseMonitorRunner(String monitorId) {

    }
}


