package ro.kudostech.pingpatrol.modules.monitor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ro.kudostech.pingpatrol.TestDefaultData;
import ro.kudostech.pingpatrol.api.server.model.CreateMonitorRequest;
import ro.kudostech.pingpatrol.api.server.model.Monitor;
import ro.kudostech.pingpatrol.api.server.model.MonitorStatus;
import ro.kudostech.pingpatrol.common.CommonMapper;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorDbo;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRepository;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRunRepository;
import ro.kudostech.pingpatrol.modules.monitor.domain.service.MonitorPatchService;
import ro.kudostech.pingpatrol.modules.monitor.domain.service.MonitorRunnerScheduler;
import ro.kudostech.pingpatrol.modules.monitor.domain.service.MonitorServiceImpl;

@ApplicationModuleTest
class MonitorServiceTest {

  @MockBean CommonMapper commonMapper;
  @MockBean MonitorRepository monitorRepository;
  @MockBean MonitorRunRepository monitorRunRepository;
  @MockBean MonitorRunnerScheduler monitorRunnerScheduler;
  @MockBean MonitorPatchService monitorPatchService;

  @MockBean private Authentication authentication;
  @MockBean private SecurityContext securityContext;

  @Autowired private MonitorServiceImpl cut;

  @BeforeEach
  public void setUp() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("Auth Name");
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void whenCreateMonitor_thenMonitorIsCreatedWithStatusRunningAndMonitorIsScheduled() {
    // Arrange
    CreateMonitorRequest createMonitorRequest =
        CreateMonitorRequest.builder()
            .name(TestDefaultData.MONITOR_NAME)
            .type(TestDefaultData.MONITOR_TYPE)
            .url(TestDefaultData.MONITOR_URL)
            .monitoringInterval(TestDefaultData.MONITORING_INTERVAL)
            .monitorTimeout(TestDefaultData.MONITOR_TIMEOUT)
            .build();

    // Act
    Monitor result = cut.createMonitor(createMonitorRequest);

    // Assert
    verify(monitorRepository).save(any());
    verify(monitorRunnerScheduler).scheduleMonitorRunner(result);
    assertThat(result.getStatus()).isEqualTo(MonitorStatus.RUNNING);
  }

  @Test
  void whenPauseMonitor_thenMonitorIsPausedFromMonitorSchedulerAndHaveStatusPaused() {
    // Arrange
    MonitorDbo monitorDbo =
        MonitorDbo.builder()
            .id(TestDefaultData.MONITOR_ID.toString())
            .name(TestDefaultData.MONITOR_NAME)
            .type(TestDefaultData.MONITOR_TYPE.name())
            .status(MonitorStatus.RUNNING.name())
            .url(TestDefaultData.MONITOR_URL)
            .monitoringInterval(TestDefaultData.MONITORING_INTERVAL)
            .monitorTimeout(TestDefaultData.MONITOR_TIMEOUT)
            .build();
    when(monitorRepository.findById(TestDefaultData.MONITOR_ID.toString()))
        .thenReturn(Optional.ofNullable(monitorDbo));

    // Act
    Monitor monitor = cut.pauseMonitorById(TestDefaultData.MONITOR_ID.toString());

    // Assert
    verify(monitorRunnerScheduler).pauseMonitorRunner(TestDefaultData.MONITOR_ID.toString());
    assertThat(monitor.getStatus()).isEqualTo(MonitorStatus.PAUSED);
  }

  @Test
  void whenResumeMonitor_thenMonitorIsResumedInMonitorSchedulerAndHaveStatusRunning() {
    // Arrange
    MonitorDbo monitorDbo =
        MonitorDbo.builder()
            .id(TestDefaultData.MONITOR_ID.toString())
            .name(TestDefaultData.MONITOR_NAME)
            .type(TestDefaultData.MONITOR_TYPE.name())
            .status(MonitorStatus.RUNNING.name())
            .url(TestDefaultData.MONITOR_URL)
            .monitoringInterval(TestDefaultData.MONITORING_INTERVAL)
            .monitorTimeout(TestDefaultData.MONITOR_TIMEOUT)
            .build();
    when(monitorRepository.findById(TestDefaultData.MONITOR_ID.toString()))
        .thenReturn(Optional.ofNullable(monitorDbo));

    // Act
    Monitor monitor = cut.resumeMonitorById(TestDefaultData.MONITOR_ID.toString());

    // Assert
    verify(monitorRunnerScheduler).scheduleMonitorRunner(any(Monitor.class));
    assertThat(monitor.getStatus()).isEqualTo(MonitorStatus.RUNNING);
  }
}
