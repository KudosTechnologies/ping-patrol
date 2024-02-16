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
    PING = "PING",
    HTTPS = "HTTPS",
}

export enum MonitorStatus {
    RUNNING = "RUNNING",
    PAUSED = "PAUSED",
    DOWN = "DOWN",
}

export type MonitorRun = {
    id: number;
    monitorId: string;
    status: MonitorRunStatus;
    duration: number;
    startedAt: string;
    errorDetails: string;
};

export enum MonitorRunStatus {
    REACHABLE = "REACHABLE",
    UNREACHABLE = "UNREACHABLE",
}
