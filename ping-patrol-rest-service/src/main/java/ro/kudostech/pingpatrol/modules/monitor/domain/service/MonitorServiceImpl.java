package ro.kudostech.pingpatrol.modules.monitor.domain.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.data.domain.Limit;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.kudostech.pingpatrol.api.server.model.CreateMonitorRequest;
import ro.kudostech.pingpatrol.api.server.model.Monitor;
import ro.kudostech.pingpatrol.api.server.model.MonitorPatchOperation;
import ro.kudostech.pingpatrol.api.server.model.MonitorRun;
import ro.kudostech.pingpatrol.api.server.model.MonitorStatus;
import ro.kudostech.pingpatrol.api.server.model.UpdateMonitorRequest;
import ro.kudostech.pingpatrol.common.exception.PropertyPathProvider;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorDbo;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRepository;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRunRepository;
import ro.kudostech.pingpatrol.modules.monitor.domain.mapper.MonitorMapper;
import ro.kudostech.pingpatrol.modules.monitor.domain.mapper.MonitorRunMapper;
import ro.kudostech.pingpatrol.modules.monitor.ports.MonitorService;

import java.util.List;
import java.util.Set;

import static jakarta.validation.Validation.byProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService {

  private static final String MONITOR_NOT_FOUND = "Monitor not found";

  private final MonitorMapper monitorMapper;
  private final MonitorRunMapper monitorRunMapper;
  private final MonitorRepository monitorRepository;
  private final MonitorRunRepository monitorRunRepository;
  private final MonitorRunnerScheduler monitorRunnerScheduler;
  private final MonitorPatchService monitorPatchService;

  @Override
  @Transactional
  public Monitor createMonitor(CreateMonitorRequest createMonitorRequest) {
    var authenticatedUserId = getAuthenticatedUserId();

    var monitorDbo =
        MonitorDbo.builder()
            .userId(authenticatedUserId)
            .name(createMonitorRequest.getName())
            .type(createMonitorRequest.getType().name())
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
            .orElseThrow(() -> new NotFoundException(MONITOR_NOT_FOUND)));
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
            .orElseThrow(() -> new NotFoundException(MONITOR_NOT_FOUND));
    monitorDbo.setName(updateMonitorRequest.getName());
    monitorDbo.setType(updateMonitorRequest.getType().name());
    monitorDbo.setUrl(updateMonitorRequest.getUrl().toString());
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
            .orElseThrow(() -> new NotFoundException(MONITOR_NOT_FOUND));
    monitorDbo.setStatus(MonitorStatus.RUNNING.name());
    monitorRepository.save(monitorDbo);
    Monitor monitor = monitorMapper.toMonitor(monitorDbo);
    monitorRunnerScheduler.scheduleMonitorRunner(monitor);
    return monitor;
  }

  @Override
  public Monitor pauseMonitorById(String monitorId) {
    var monitorDbo =
        monitorRepository
            .findById(monitorId)
            .orElseThrow(() -> new NotFoundException(MONITOR_NOT_FOUND));
    monitorDbo.setStatus(MonitorStatus.PAUSED.name());
    monitorRepository.save(monitorDbo);
    Monitor monitor = monitorMapper.toMonitor(monitorDbo);
    monitorRunnerScheduler.pauseMonitorRunner(monitor.getId().toString());
    return monitor;
  }

  @Override
  public List<MonitorRun> getAllMonitorRuns(String monitorId) {
    return monitorRunMapper.toMonitorRuns(
        monitorRunRepository.findAllByMonitorId(monitorId, Limit.of(20)));
  }

  @Override
  @Transactional
  public void deleteMonitorRuns(String monitorId) {
    monitorRunRepository.deleteAllByMonitorId(monitorId);
  }

  @Transactional
  public Monitor patchMonitorById(
      String monitorId, List<MonitorPatchOperation> monitorPatchOperation) {
    MonitorDbo monitorDbo =
        monitorRepository
            .findById(monitorId)
            .orElseThrow(() -> new NotFoundException(MONITOR_NOT_FOUND));
    Set<ConstraintViolation<?>> violations =
        monitorPatchService.patch(monitorDbo, monitorPatchOperation);

    try (ValidatorFactory validatorFactory =
        byProvider(HibernateValidator.class)
            .configure()
            .propertyNodeNameProvider(new PropertyPathProvider())
            .buildValidatorFactory()) {
      violations.addAll(validatorFactory.getValidator().validate(monitorDbo));
    }

    if (!violations.isEmpty()) {
      log.info(
          "Could not validate patches for user {}.\nFound the following violations: {}",
          monitorDbo,
          violations);
      throw new ConstraintViolationException("violations", violations);
    } else {
      log.debug("Successfully validated patches for user {}.", monitorDbo);
    }
    return monitorMapper.toMonitor(monitorDbo);
  }

  private String getAuthenticatedUserId() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
