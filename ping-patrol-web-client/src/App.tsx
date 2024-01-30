import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomeWrapperPage from "./components/presentation/HomeWrapper";
import MonitoringPage from "./components/presentation/Monitoring";
import IntegrationPage from "./components/presentation/Integration";
import DashboardPage from "./components/dashboard/DashboardMenu";
import Monitors from "./components/dashboard/Monitors";
import Dashboard from "./components/dashboard/Dashboard";
import Incidents from "./components/dashboard/Incidents";
import PrivateRoute from "./utils/PrivateRoute";

const App: React.FC = () => (
  <Router>
    <Routes>
      <Route path="/" element={<HomeWrapperPage />}>
        <Route index element={<MonitoringPage />} /> {/* Add index attribute */}
        <Route path="/monitoring" element={<MonitoringPage />} />
        <Route path="/integration" element={<IntegrationPage />} />
      </Route>
      <Route path="/dashboard" element={<DashboardPage />}>
        <Route
          index
          element={
            <PrivateRoute>
              {" "}
              <Dashboard />
            </PrivateRoute>
          }
        />
        <Route path="monitors" element={<Monitors />} />
        <Route path="incidents" element={<Incidents />} />
      </Route>
      <Route path="*" element={<div>404</div>} />
    </Routes>
  </Router>
);

export default App;
