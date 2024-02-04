package ro.kudostech.pingpatrol.modules.monitor.domain.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.kudostech.pingpatrol.api.server.model.Monitor;
import ro.kudostech.pingpatrol.common.CommonMapper;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorDbo;

import java.util.List;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {CommonMapper.class})
public interface MonitorMapper {

  @Mapping(source = "id", target = "id", qualifiedByName = "stringToUUID")
  Monitor toMonitor(MonitorDbo monitorDbo);

  @Mapping(source = "id", target = "id", qualifiedByName = "UUIDToString")
  MonitorDbo toMonitorDbo(Monitor monitor);

  List<Monitor> toMonitors(List<MonitorDbo> monitorDbos);
}
