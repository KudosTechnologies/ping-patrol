package ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "monitor")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorDbo {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotNull private String userId;

  @NotNull
  @Column(unique = true)
  private String name;

  @NotNull private String monitorType;

  @NotNull private String url;

  @NotNull private Integer monitoringInterval;

  @NotNull private Integer monitorTimeout;
}
