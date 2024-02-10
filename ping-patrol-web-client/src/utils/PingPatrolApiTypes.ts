export type Monitor = {
  id: string;
  name: string;
  type: MonitorType;
  status: MonitorStatus;
  url: string;
  monitoringInterval: number;
  monitorTimeout: number;
};

export enum MonitorType {
  PING,
  HTTPS,
}

export enum MonitorStatus {
  RUNNING = "RUNNING",
  PAUSED = "PAUSED",
  STOPPED = "STOPPED",
}
