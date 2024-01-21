import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  IconButton,
  Box,
  styled,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const StyledToolbar = styled(Toolbar)(({ theme }) => ({
  gap: theme.spacing(2),
}));

const StyledLogo = styled("div")(({ theme }) => ({
  display: "flex",
  alignItems: "center",
  marginRight: theme.spacing(5),
  // gap: theme.spacing(1),
}));

const TopMenuBar: React.FC = () => {
  const navigate = useNavigate();

  return (
    <AppBar position="sticky" color="primary">
      <StyledToolbar>
        <StyledLogo>
          <IconButton edge="start" color="inherit" aria-label="menu">
            <img
              src="src/assets/ping-patrol-logo-color.png"
              alt="Logo"
              style={{ height: "60px" }}
            />
          </IconButton>
          <Typography variant="h5">PingPatrol</Typography>
        </StyledLogo>
        <Button color="inherit" onClick={() => navigate("/monitoring")}>
          Monitoring
        </Button>
        <Button color="inherit" onClick={() => navigate("/integration")}>
          Integration
        </Button>
        {/* <Button color="inherit">Status Page</Button> */}
        <Box sx={{ flexGrow: 1 }} />
        <Button color="inherit">Login</Button>
      </StyledToolbar>
    </AppBar>
  );
};

export default TopMenuBar;
