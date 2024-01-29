import React from "react";
import TopMenuBar from "./TopMenuBar";
import { Outlet } from "react-router-dom";

const HomeWrapperPage: React.FC = () => {
  return (
    <div>
      <TopMenuBar />
      <Outlet />
    </div>
  );
};

export default HomeWrapperPage;
