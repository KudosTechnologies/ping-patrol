import axios from "axios";
import {Monitor} from "./PingPatrolApiTypes";

const PING_PATROL_BASE_URL = "http://localhost:8080";

const instance = axios.create({
    baseURL: PING_PATROL_BASE_URL,
    timeout: 1000,
    headers: {
        "Content-Type": "application/json",
    },
});

export const pingPatrolApi = {
    getMonitors,
    createMonitor,
    updateMonitor,
    pauseMonitor,
    resumeMonitor,
    deleteMonitor,
};

function getMonitors(token: string): Promise<Monitor[]> {
    return instance
        .get("/v1/monitors", {
            headers: {Authorization: bearerToken(token)},
        })
        .then((response) => response.data);
}

function createMonitor(token: string, monitor: Monitor): Promise<Monitor> {
    return instance
        .post("/v1/monitors", monitor, {
            headers: {Authorization: bearerToken(token)},
        })
        .then((response) => response.data);
}

function updateMonitor(token: string, monitor: Monitor): Promise<Monitor> {
    return instance
        .put(`/v1/monitors/${monitor.id}`, monitor, {
            headers: {Authorization: bearerToken(token)},
        })
        .then((response) => response.data);

}

function pauseMonitor(token: string, monitorId: string): Promise<Monitor> {
    return instance
        .put(`/v1/monitors/${monitorId}/pause`, {}, {
            headers: {Authorization: bearerToken(token)},
        })
        .then((response) => response.data);
}

function resumeMonitor(token: string, monitorId: string): Promise<Monitor> {
    return instance
        .put(`/v1/monitors/${monitorId}/resume`, {}, {
            headers: {Authorization: bearerToken(token)},
        })
        .then((response) => response.data);
}

function deleteMonitor(token: string, monitorId: string): Promise<void> {
    return instance
        .delete(`/v1/monitors/${monitorId}`, {
            headers: {Authorization: bearerToken(token)},
        })
        .then((response) => response.data);

}

function bearerToken(token: string) {
    return `Bearer ${token}`;
}
