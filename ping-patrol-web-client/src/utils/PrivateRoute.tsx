import React from "react";
import { useKeycloak } from "@react-keycloak/web";

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const { keycloak, initialized } = useKeycloak();

  if (!initialized) {
    return <div>Loading...</div>;
  }

  if (!keycloak.authenticated) {
    keycloak.login();
  }
  return <React.Fragment>{children}</React.Fragment>;
}

export default PrivateRoute;
