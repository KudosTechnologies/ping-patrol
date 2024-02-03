package ro.kudostech.pingpatrol.modules.monitor.domain.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.kudostech.pingpatrol.api.server.model.CreateMonitorRequest;
import ro.kudostech.pingpatrol.api.server.model.Monitor;
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

  @Override
  @Transactional
  public Monitor createMonitor(CreateMonitorRequest createMonitorRequest) {
    var authenticatedUserId = getAuthenticatedUserId();

    var monitorDbo =
        MonitorDbo.builder()
            .userId(authenticatedUserId)
            .name(createMonitorRequest.getName())
            .monitorType(createMonitorRequest.getMonitorType().name())
            .url(createMonitorRequest.getUrl())
            .monitoringInterval(createMonitorRequest.getMonitoringInterval())
            .monitorTimeout(createMonitorRequest.getMonitorTimeout())
            .build();
    monitorDbo.setUserId(authenticatedUserId);
    monitorRepository.save(monitorDbo);
    return monitorMapper.toMonitor(monitorDbo);
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

  private String getAuthenticatedUserId() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
