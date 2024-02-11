package ro.kudostech.pingpatrol.modules.monitor.domain.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.kudostech.pingpatrol.api.server.model.MonitorRun;
import ro.kudostech.pingpatrol.common.CommonMapper;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRunDbo;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {CommonMapper.class})
public interface MonitorRunMapper {

  @Mapping(source = "monitorId", target = "monitorId", qualifiedByName = "stringToUUID")
  MonitorRun toMonitorRun(MonitorRunDbo monitorRunDbo);

  @Mapping(source = "monitorId", target = "monitorId", qualifiedByName = "UUIDToString")
  MonitorRunDbo toMonitorRunDbo(MonitorRun monitorRun);

  List<MonitorRun> toMonitorRuns(List<MonitorRunDbo> monitorRunDbos);
}
