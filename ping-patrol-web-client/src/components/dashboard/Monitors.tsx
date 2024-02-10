import { useEffect, useState } from "react";
import { pingPatrolApi } from "../../utils/PingPatrolApi";
import { useKeycloak } from "@react-keycloak/web";
import { handleLogError } from "../../utils/Helpers";
import EditTwoToneIcon from "@mui/icons-material/EditTwoTone";
import DeleteForeverTwoToneIcon from "@mui/icons-material/DeleteForeverTwoTone";
import PauseCircleFilledTwoToneIcon from "@mui/icons-material/PauseCircleFilledTwoTone";
import AddCircleOutlineTwoToneIcon from "@mui/icons-material/AddCircleOutlineTwoTone";
import {
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  IconButton,
  Button,
  Box,
  Container,
  Paper,
} from "@mui/material";
import { Monitor } from "../../utils/PingPatrolApiTypes";

const Monitors = () => {
  const [monitors, setMonitors] = useState<Monitor[]>([]);

  const { keycloak } = useKeycloak();

  useEffect(() => {
    handleGetMonitors();
  }, []);

  const handleGetMonitors = async () => {
    try {
      const response = await pingPatrolApi.getMonitors(keycloak.token!); // Ensure keycloak.token is not undefined
      setMonitors(response);
      console.log(response);
    } catch (error) {
      handleLogError(error);
    }
  };

  return (
    <Container>
      <Paper elevation={3} sx={{ margin: "16px 0", overflowX: "auto" }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Type</TableCell>
              <TableCell>URL</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {monitors.map((monitor) => (
              <TableRow key={monitor.id}>
                <TableCell>{monitor.name}</TableCell>
                <TableCell>{monitor.type}</TableCell>
                <TableCell>{monitor.url}</TableCell>
                <TableCell>{monitor.status}</TableCell>
                <TableCell>
                  <IconButton>
                    <PauseCircleFilledTwoToneIcon />
                  </IconButton>
                  <IconButton>
                    <EditTwoToneIcon />
                  </IconButton>
                  <IconButton>
                    <DeleteForeverTwoToneIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>
      <Box sx={{ display: "flex", justifyContent: "center", padding: "20px" }}>
        <Button
          startIcon={<AddCircleOutlineTwoToneIcon />}
          variant="contained"
          color="primary"
        >
          Add Monitor
        </Button>
      </Box>
    </Container>
  );
};

export default Monitors;
