import axios from "axios";
import { Monitor } from "./PingPatrolApiTypes";

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
};

function getMonitors(token: string): Promise<Monitor[]> {
  return instance
    .get("/v1/monitors", {
      headers: { Authorization: bearerToken(token) },
    })
    .then((response) => response.data);
}

function bearerToken(token: string) {
  return `Bearer ${token}`;
}
