import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomeWrapperPage from "./components/presentation/HomeWrapper";
import MonitoringPage from "./components/presentation/Monitoring";
import IntegrationPage from "./components/presentation/Integration";
import DashboardPage from "./components/dashboard/DashboardMenu";
import Monitors from "./components/dashboard/Monitors";

const App: React.FC = () => (
  <Router>
    <Routes>
      <Route path="/" element={<HomeWrapperPage />}>
        <Route index element={<MonitoringPage />} /> {/* Add index attribute */}
        <Route path="/monitoring" element={<MonitoringPage />} />
        <Route path="/integration" element={<IntegrationPage />} />
      </Route>
      <Route path="/dashboard" element={<DashboardPage />}>
        {/* <Route index element={<DashboardPage />} /> */}
        <Route path="monitors" element={<Monitors />} />
      </Route>
      <Route path="*" element={<div>404</div>} />
    </Routes>
  </Router>
);

export default App;
