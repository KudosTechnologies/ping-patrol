import React, { useState } from "react";
import {
  AppBar,
  Toolbar,
  Button,
  IconButton,
  styled,
  Drawer,
  List,
  ListItem,
  ListItemText,
  useTheme,
  useMediaQuery,
  Typography,
  Box,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import { useNavigate } from "react-router-dom";

const StyledToolbar = styled(Toolbar)(() => ({
  justifyContent: "space-between",
}));

const TopMenuBar: React.FC = () => {
  const navigate = useNavigate();
  const [drawerOpen, setDrawerOpen] = useState(false);
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("md"));

  const handleDrawerToggle = () => {
    setDrawerOpen(!drawerOpen);
  };

  const menuItems = [
    { text: "Monitoring", path: "/monitoring" },
    { text: "Integration", path: "/integration" },
    // Add other menu items here as needed
  ];

  const renderMobileMenu = () => (
    <>
      <IconButton
        edge="start"
        color="inherit"
        aria-label="open drawer"
        onClick={handleDrawerToggle}
      >
        <MenuIcon />
      </IconButton>
      <Drawer anchor="left" open={drawerOpen} onClose={handleDrawerToggle}>
        <List>
          {menuItems.map((item) => (
            <ListItem
              button
              key={item.text}
              onClick={() => {
                handleDrawerToggle();
                navigate(item.path);
              }}
            >
              <ListItemText primary={item.text} />
            </ListItem>
          ))}
        </List>
      </Drawer>
      <Button color="inherit" onClick={() => navigate("/login")}>
        Login
      </Button>
    </>
  );

  const renderDesktopMenu = () => (
    <>
      <Box sx={{ flexGrow: 0.5 }}>
        {menuItems.map((item) => (
          <Button
            key={item.text}
            color="inherit"
            onClick={() => navigate(item.path)}
          >
            {item.text}
          </Button>
        ))}
      </Box>
      <Box sx={{ marginRight: "10%" }}>
        <Button color="inherit" onClick={() => navigate("/login")}>
          Login
        </Button>
      </Box>
    </>
  );

  return (
    <AppBar position="sticky" style={{ backgroundColor: '#131A25' }}>
      <StyledToolbar>
        <div style={{ justifyContent: "space-between" }}>
          <Box display="flex" alignItems="center" sx={{ marginLeft: "80%" }}>
            <IconButton edge="start" color="inherit" aria-label="logo" onClick={() => navigate("/monitoring")}>
              <img
                src="src/assets/ping-patrol-logo-color.png"
                alt="Logo"
                style={{ height: "100px", objectFit: "contain" }}
              />
            </IconButton>
            <Typography
              variant="h6"
              component="div"
              sx={{
                display: { xs: "none", md: "block" },
                whiteSpace: "nowrap",
                fontWeight: "bold",
              }}
            >
              Ping Patrol
            </Typography>
          </Box>
        </div>
        {isMobile ? renderMobileMenu() : renderDesktopMenu()}
      </StyledToolbar>
    </AppBar>
  );
};

export default TopMenuBar;
