import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.tsx";
import "./index.css";
import keycloak from "./config/auth.ts";
import { ReactKeycloakProvider } from "@react-keycloak/web";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <ReactKeycloakProvider authClient={keycloak}>
    <React.StrictMode>
      <App />
    </React.StrictMode>
  </ReactKeycloakProvider>
);
