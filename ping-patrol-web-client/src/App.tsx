import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomeWrapperPage from "./components/HomeWrapper";
import MonitoringPage from "./components/Monitoring";
import IntegrationPage from "./components/Integration";
import DashboardPage from "./components/Dashboard";

const App: React.FC = () => (
  <Router>
    <Routes>
      <Route path="/" element={<HomeWrapperPage />}>
        <Route index element={<MonitoringPage />} /> {/* Add index attribute */}
        <Route path="/monitoring" element={<MonitoringPage />} />
        <Route path="/integration" element={<IntegrationPage />} />
      </Route>
      <Route path="/dashboard" element={<DashboardPage />} />
      <Route path="*" element={<div>404</div>} />
    </Routes>
  </Router>
);

export default App;
