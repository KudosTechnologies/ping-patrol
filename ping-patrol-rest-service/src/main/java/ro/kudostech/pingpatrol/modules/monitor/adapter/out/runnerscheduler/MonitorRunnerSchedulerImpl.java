package ro.kudostech.pingpatrol.modules.monitor.adapter.out.runnerscheduler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import ro.kudostech.pingpatrol.api.server.model.Monitor;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRepository;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRunRepository;
import ro.kudostech.pingpatrol.modules.monitor.domain.service.MonitorRunnerScheduler;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonitorRunnerSchedulerImpl implements MonitorRunnerScheduler {

  private final TaskScheduler taskScheduler;
  private final MonitorRunRepository monitorRunRepository;
  private final MonitorRepository monitorRepository;
  private final Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

  @Override
  public void scheduleMonitorRunner(Monitor monitor) {
    if (scheduledTasks.containsKey(monitor.getId().toString())) {
      throw new IllegalStateException("Monitor already scheduled");
    }
    ScheduledFuture<?> scheduledFuture =
        taskScheduler.scheduleWithFixedDelay(
            new HttpMonitorRunner(
                monitor.getId().toString(),
                monitor.getUrl().toString(),
                monitor.getMonitorTimeout(),
                monitorRunRepository),
            Duration.ofSeconds(monitor.getMonitoringInterval()));
    scheduledTasks.put(monitor.getId().toString(), scheduledFuture);
  }

  @Override
  public void pauseMonitorRunner(String monitorId) {
    ScheduledFuture<?> scheduledFuture = scheduledTasks.get(monitorId);
    if (scheduledFuture != null) {
      scheduledFuture.cancel(true);
      scheduledTasks.remove(monitorId);
    } else {
      log.info("Monitor with id {} is not scheduled", monitorId);
    }
  }
}
