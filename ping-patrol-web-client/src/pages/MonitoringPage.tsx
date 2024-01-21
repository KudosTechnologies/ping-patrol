import {
  Box,
  Typography,
  Button,
  useMediaQuery,
  useTheme,
} from "@mui/material";

const Monitoring = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  return (
    <Box
      sx={{
        backgroundColor: "#131A25",
        color: "white",
        width: "100%", // Full width
        height: "100vh", // Full viewport height
        display: "flex", // To center the content
        flexDirection: "column", // Stack the content vertically
        justifyContent: "center", // Center the content vertically
        alignItems: "center", // Center the content horizontally
        textAlign: "center", // Center the text horizontally
      }}
    >
      <Typography variant={isMobile ? "h5" : "h2"} gutterBottom>
        The world's leading
        <span style={{ color: "#3AD671" }}> uptime monitoring </span>
        service
        <span style={{ color: "#3AD671" }}>.</span>
      </Typography>
      <Typography variant={isMobile ? "subtitle1" : "h5"} gutterBottom>
        Get 50 monitors with 5-minute checks{" "}
        <span style={{ color: "#3AD671" }}> totally FREE </span>.
      </Typography>
      <Button
        variant="contained"
        color="success"
        size={isMobile ? "medium" : "large"}
      >
        <Typography variant="h6">Start monitoring in 30 seconds</Typography>
      </Button>
    </Box>
  );
};

export default Monitoring;
