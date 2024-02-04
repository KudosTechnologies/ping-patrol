package ro.kudostech.pingpatrol.modules.monitor.domain.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.kudostech.pingpatrol.api.server.model.CreateMonitorRequest;
import ro.kudostech.pingpatrol.api.server.model.Monitor;
import ro.kudostech.pingpatrol.api.server.model.MonitorStatus;
import ro.kudostech.pingpatrol.api.server.model.MonitorType;
import ro.kudostech.pingpatrol.api.server.model.UpdateMonitorRequest;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorDbo;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRepository;
import ro.kudostech.pingpatrol.modules.monitor.domain.mapper.MonitorMapper;
import ro.kudostech.pingpatrol.modules.monitor.ports.MonitorService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService {

  private final MonitorMapper monitorMapper;
  private final MonitorRepository monitorRepository;
  private final MonitorRunnerScheduler monitorRunnerScheduler;

  @Override
  @Transactional
  public Monitor createMonitor(CreateMonitorRequest createMonitorRequest) {
    var authenticatedUserId = getAuthenticatedUserId();

    var monitorDbo =
        MonitorDbo.builder()
            .userId(authenticatedUserId)
            .name(createMonitorRequest.getName())
            .type(createMonitorRequest.getMonitorType().name())
            .status(MonitorStatus.RUNNING.name())
            .url(createMonitorRequest.getUrl())
            .monitoringInterval(createMonitorRequest.getMonitoringInterval())
            .monitorTimeout(createMonitorRequest.getMonitorTimeout())
            .build();
    monitorDbo.setUserId(authenticatedUserId);
    monitorRepository.save(monitorDbo);
    Monitor monitor = monitorMapper.toMonitor(monitorDbo);
    monitorRunnerScheduler.scheduleMonitorRunner(monitor);
    return monitor;
  }

  @Override
  @Transactional(readOnly = true)
  public Monitor getMonitorById(String monitorId) {
    return monitorMapper.toMonitor(
        monitorRepository
            .findById(monitorId)
            .orElseThrow(() -> new NotFoundException("Monitor not found")));
  }

  @Override
  public List<Monitor> getAllMonitors() {
    return monitorMapper.toMonitors(monitorRepository.findAll());
  }

  @Override
  @Transactional
  public void deleteMonitorById(String monitorId) {
    monitorRepository.deleteById(monitorId);
  }

  @Override
  public Monitor updateMonitor(String monitorId, UpdateMonitorRequest updateMonitorRequest) {
    var monitorDbo =
        monitorRepository
            .findById(monitorId)
            .orElseThrow(() -> new NotFoundException("Monitor not found"));
    monitorDbo.setName(updateMonitorRequest.getName());
    monitorDbo.setUrl(updateMonitorRequest.getUrl());
    monitorDbo.setMonitoringInterval(updateMonitorRequest.getMonitoringInterval());
    monitorDbo.setMonitorTimeout(updateMonitorRequest.getMonitorTimeout());
    monitorRepository.save(monitorDbo);
    Monitor monitor = monitorMapper.toMonitor(monitorDbo);
    monitorRunnerScheduler.pauseMonitorRunner(monitor.getId().toString());
    monitorRunnerScheduler.scheduleMonitorRunner(monitor);
    return monitorMapper.toMonitor(monitorDbo);
  }

  @Override
  @Transactional
  public Monitor resumeMonitorById(String monitorId) {
    var monitorDbo =
        monitorRepository
            .findById(monitorId)
            .orElseThrow(() -> new NotFoundException("Monitor not found"));
    monitorDbo.setStatus(MonitorStatus.RUNNING.name());
    monitorRepository.save(monitorDbo);
    Monitor monitor = monitorMapper.toMonitor(monitorDbo);
    monitorRunnerScheduler.scheduleMonitorRunner(monitor);
    return monitor;
  }

  @Override
  public Monitor pauseMonitorById(String monitorId) {
    return null;
  }

  private String getAuthenticatedUserId() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
