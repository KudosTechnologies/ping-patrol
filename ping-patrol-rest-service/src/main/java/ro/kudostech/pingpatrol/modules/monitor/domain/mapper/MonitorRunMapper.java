package ro.kudostech.pingpatrol.modules.monitor.domain.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import ro.kudostech.pingpatrol.api.server.model.MonitorRun;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRunDbo;

@Mapper(componentModel = "spring")
public interface MonitorRunMapper {
  MonitorRun toMonitorRun(MonitorRunDbo monitorRunDbo);

  MonitorRunDbo toMonitorRunDbo(MonitorRun monitorRun);

  List<MonitorRun> toMonitorRuns(List<MonitorRunDbo> monitorRunDbos);
}
