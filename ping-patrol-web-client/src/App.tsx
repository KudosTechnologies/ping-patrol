import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomeWrapperPage from "./pages/HomeWrapperPage";
import MonitoringPage from "./pages/MonitoringPage";
import IntegrationPage from "./pages/IntegrationPage";
import DashboardPage from "./pages/Dashboard";

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
