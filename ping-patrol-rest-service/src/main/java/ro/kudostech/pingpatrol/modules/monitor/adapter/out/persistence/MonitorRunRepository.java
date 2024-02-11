package ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorRunDbo;

import java.util.List;

@Repository
public interface MonitorRunRepository extends JpaRepository<MonitorRunDbo, String> {
    List<MonitorRunDbo> findAllByMonitorId(String monitorId, Limit limit);

  void deleteAllByMonitorId(String monitorId);
}
