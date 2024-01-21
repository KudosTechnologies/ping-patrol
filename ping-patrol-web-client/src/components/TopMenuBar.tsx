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

const StyledToolbar = styled(Toolbar)(({ theme }) => ({
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  gap: theme.spacing(1),
}));

const StyledLogoContainer = styled("div")(() => ({
  display: "flex",
  alignItems: "center",
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

  const drawer = (
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
  );

  return (
    <AppBar position="sticky" color="primary">
      <StyledToolbar>
        {isMobile && (
          <IconButton
            edge="start"
            color="inherit"
            aria-label="open drawer"
            onClick={handleDrawerToggle}
          >
            <MenuIcon />
          </IconButton>
        )}
        <StyledLogoContainer onClick={() => navigate("/")}>
          <IconButton edge="start" color="inherit" aria-label="logo">
            <img
              src="src/assets/ping-patrol-logo-color.png"
              alt="Logo"
              style={{ height: "60px" }}
            />
          </IconButton>
          <Typography
            variant="h6"
            component="div"
            sx={{ display: { xs: "none", md: "block" } }}
          >
            Ping Patrol
          </Typography>
        </StyledLogoContainer>
        {isMobile ? (
          <Button color="inherit" onClick={() => navigate("/login")}>
            Login
          </Button>
        ) : (
          <>
            {!isMobile && <Box sx={{ flexGrow: 0.1 }} />}

            {menuItems.map((item) => (
              <Button
                key={item.text}
                color="inherit"
                onClick={() => navigate(item.path)}
              >
                {item.text}
              </Button>
            ))}
            <Box sx={{ flexGrow: 1 }} />
            <Button color="inherit" onClick={() => navigate("/login")}>
              Login
            </Button>
          </>
        )}
        {isMobile && drawer}
      </StyledToolbar>
    </AppBar>
  );
};

export default TopMenuBar;
