import React from "react";
import keycloak from "./config/auth.ts";
import { ReactKeycloakProvider } from "@react-keycloak/web";
import MyComponent from "./components/MyComponent.tsx";

const App: React.FC = () => (
  // <ReactKeycloakProvider authClient={keycloak}>
    <MyComponent></MyComponent>
  // </ReactKeycloakProvider>
);

export default App;
