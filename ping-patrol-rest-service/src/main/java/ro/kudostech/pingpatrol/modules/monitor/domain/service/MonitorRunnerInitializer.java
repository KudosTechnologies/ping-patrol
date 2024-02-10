package ro.kudostech.pingpatrol.modules.monitor.domain.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRepository;
import ro.kudostech.pingpatrol.modules.monitor.domain.mapper.MonitorMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonitorRunnerInitializer {

  private final MonitorRunnerScheduler monitorRunnerScheduler;
  private final MonitorMapper monitorMapper;
  private final MonitorRepository monitorRepository;

  @PostConstruct
  public void initializeMonitors() {
    log.info("Initializing monitors");
    monitorRepository
        .findAll()
        .forEach(
            monitorDbo ->
                monitorRunnerScheduler.scheduleMonitorRunner(monitorMapper.toMonitor(monitorDbo)));
  }
}
