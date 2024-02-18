package ro.kudostech.pingpatrol;

import lombok.experimental.UtilityClass;
import ro.kudostech.pingpatrol.api.server.model.MonitorType;

import java.util.UUID;

@UtilityClass
public class TestDefaultData {

  public UUID MONITOR_ID = UUID.fromString("89441990-7b3e-496a-8cde-76c5fd69e3c6");
  public String AUTH_NAME = "Auth Name";
  public String MONITOR_NAME = "kudostech";
  public MonitorType MONITOR_TYPE = MonitorType.HTTPS;
  public String MONITOR_URL = "https://kudostech.ro";
  public int MONITORING_INTERVAL = 5;
  public int MONITOR_TIMEOUT = 10;
}
