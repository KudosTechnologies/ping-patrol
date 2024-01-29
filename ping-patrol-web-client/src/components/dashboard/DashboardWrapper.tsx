import { Dashboard } from "@mui/icons-material";
import { Outlet } from "react-router-dom";

const DashboardWrapper = () => {
  return (
    <div>
      <Dashboard />
      <Outlet />
    </div>
  );
};

export default DashboardWrapper;
