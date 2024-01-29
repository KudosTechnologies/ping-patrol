import {
  Box,
  Typography,
  useMediaQuery,
  useTheme,
  Grid,
  Card,
  CardMedia,
  CardContent,
} from "@mui/material";

const IntegrationPage = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  return (
    <>
      <Box
        sx={{
          backgroundColor: "#131A25",
          color: "white",
          width: "100%", // Full width
          height: "50vh", // Full viewport height
          display: "flex", // To center the content
          flexDirection: "column", // Stack the content vertically
          justifyContent: "center", // Center the content vertically
          alignItems: "center", // Center the content horizontally
          textAlign: "center", // Center the text horizontally
        }}
      >
        <Typography
          variant={isMobile ? "h5" : "h2"}
          gutterBottom
          sx={{ fontWeight: "bold" }}
        >
          Downtime happens
          <span style={{ color: "#3AD671" }}>.</span>
        </Typography>
        <Typography
          variant={isMobile ? "subtitle1" : "h2"}
          gutterBottom
          sx={{ fontWeight: "bold" }}
        >
          <span style={{ color: "#3AD671" }}>Get notified!</span>
        </Typography>
        <Typography variant={isMobile ? "subtitle1" : "h6"} gutterBottom>
          Everyone knows it happens. But it’s important to realize before
          customers do.
        </Typography>
      </Box>
      <Box
        sx={{
          color: "black",
          width: "100%", // Full width
          display: "flex", // To center the content
          flexDirection: "column", // Stack the content vertically
          justifyContent: "center", // Center the content vertically
          alignItems: "center", // Center the content horizontally
          textAlign: "center", // Center the text horizontally
        }}
      >
        <Typography
          variant={isMobile ? "h5" : "h4"}
          gutterBottom
          sx={{ fontWeight: "bold", paddingTop: "2rem" }}
        >
          <span style={{ color: "#3AD671" }}>Personal </span>
          notification channels
          <span style={{ color: "#3AD671" }}>.</span>
        </Typography>
        <Grid container spacing={7} justifyContent="center">
          <Grid item xs={5} sm={4} md={3} lg={1}>
            <Card sx={{ padding: "1rem", height: "100%", width:"100%" }}>
              <CardMedia
                component="img"
                image="/src/assets/integration-email-dark.svg"
                alt="Email Logo"
              />
              <CardContent>
                <Typography variant="body1" color="text.secondary">
                  E-mail
                </Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={5} sm={4} md={3} lg={1}>
            <Card sx={{ padding: "1rem", height: "100%", width:"100%" }}>
              <CardMedia
                component="img"
                image="/src/assets/integration-sms-dark.svg"
                alt="SMS Logo"
              />
              <CardContent>
                <Typography variant="body1" color="text.secondary">
                  SMS
                </Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={5} sm={4} md={3} lg={1}>
            <Card sx={{ padding: "1rem", height: "100%", width:"100%" }}>
              <CardMedia
                component="img"
                image="/src/assets/integration-push-dark.svg"
                alt="SMS Logo"
              />
              <CardContent>
                <Typography variant="body1" color="text.secondary">
                  Mobile app push
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Box>
    </>
  );
};

export default IntegrationPage;
