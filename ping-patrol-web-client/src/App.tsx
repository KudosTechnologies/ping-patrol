import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomePage from "./pages/HomePage";
import HomeWrapperPage from "./pages/HomeWrapperPage";
import MonitoringPage from "./pages/MonitoringPage";
import IntegrationPage from "./pages/IntegrationPage";

const App: React.FC = () => (
  <Router>
    <Routes>
      <Route path="/" element={<HomeWrapperPage />}>
        <Route path="/" element={<HomePage />} />
        <Route path="/monitoring" element={<MonitoringPage />} />
        <Route path="/integration" element={<IntegrationPage />} />
      </Route>

      {/* other routes */}
    </Routes>
  </Router>
);

export default App;
