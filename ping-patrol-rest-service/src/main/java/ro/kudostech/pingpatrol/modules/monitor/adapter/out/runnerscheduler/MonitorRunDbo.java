package ro.kudostech.pingpatrol.modules.monitor.adapter.out.runnerscheduler;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "monitor_run")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorRunDbo {

  @Id private long id;
  @NotNull private String monitorId;
  private String status;

  private long duration;
  @NotNull private Instant startedAt;
}
