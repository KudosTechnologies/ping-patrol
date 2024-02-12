import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {pingPatrolApi} from "../../utils/PingPatrolApi";
import {useKeycloak} from "@react-keycloak/web";
import {handleLogError} from "../../utils/Helpers";
import EditTwoToneIcon from "@mui/icons-material/EditTwoTone";
import DeleteForeverTwoToneIcon from "@mui/icons-material/DeleteForeverTwoTone";
import PauseCircleFilledTwoToneIcon from "@mui/icons-material/PauseCircleFilledTwoTone";
import AddCircleOutlineTwoToneIcon from "@mui/icons-material/AddCircleOutlineTwoTone";
import PlayCircleFilledTwoToneIcon from '@mui/icons-material/PlayCircleFilledTwoTone';
import LaunchTwoToneIcon from '@mui/icons-material/LaunchTwoTone';
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
    Paper, Tooltip,
} from "@mui/material";
import {Monitor} from "../../utils/PingPatrolApiTypes";
import MonitorFormDialog from "./MonitorFormDialog.tsx";




const MonitorsDashboard = () => {
    const [monitors, setMonitors] = useState<Monitor[]>([]);
    const {keycloak} = useKeycloak();
    const [dialogOpen, setDialogOpen] = useState(false);
    const [editingMonitor, setEditingMonitor] = useState<Monitor | undefined>(undefined);
    const navigate = useNavigate();

    useEffect(() => {
        handleGetMonitors().then();
    }, []);

    const handleGetMonitors = async () => {
        try {
            const response = await pingPatrolApi.getMonitors(keycloak.token!); // Ensure keycloak.token is not undefined
            setMonitors(response);
        } catch (error) {
            handleLogError(error);
        }
    };

    const handleOpenDialog = (monitor?: Monitor) => {
        setEditingMonitor(monitor); // If undefined, we're adding; otherwise, we're editing
        setDialogOpen(true);
    };

    const handleCloseDialog = () => {
        setDialogOpen(false);
        setEditingMonitor(undefined); // Reset editing monitor
    };

    const handleSaveMonitor = (monitor: Monitor) => {
        if (editingMonitor) {
            pingPatrolApi.updateMonitor(keycloak.token!, monitor)
                .then(() => {
                    handleGetMonitors().then();
                })
                .catch(handleLogError);
        } else {
            pingPatrolApi.createMonitor(keycloak.token!, monitor).then(() => {
                handleGetMonitors().then();
            })
        }

        // After saving, fetch monitors again to refresh the list
        handleCloseDialog();
    };

    const handlePauseMonitor = (monitorId: string) => {
        pingPatrolApi.pauseMonitor(keycloak.token!, monitorId)
            .then(() => {
                handleGetMonitors().then();
            })
            .catch(handleLogError);
    }

    const handleResumeMonitor = (monitorId: string) => {
        pingPatrolApi.resumeMonitor(keycloak.token!, monitorId)
            .then(() => {
                handleGetMonitors().then();
            })
            .catch(handleLogError);
    }

    const handleDeleteMonitor = (monitorId: string) => {
        pingPatrolApi.deleteMonitor(keycloak.token!, monitorId)
            .then(() => {
                handleGetMonitors().then();
            })
            .catch(handleLogError);
    }

    const handleMonitorClick = (monitorId: string) => {
        navigate(`/dashboard/monitorOverview/${monitorId}`);
    };


    return (
        <Container>
            <Paper elevation={3} sx={{margin: "16px 0", overflowX: "auto"}}>
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
                                <TableCell
                                    sx={{
                                        cursor: 'pointer',
                                        textDecoration: 'underline',
                                        display: 'flex',
                                        alignItems: 'center',
                                        gap: '5px',
                                        flexGrow: 1,
                                        minHeight: '80px'

                                    }}
                                    onClick={() => handleMonitorClick(monitor.id)}
                                >
                                    {monitor.name}
                                    <Tooltip title="View Details">
                                        <LaunchTwoToneIcon/>
                                    </Tooltip>
                                </TableCell>
                                <TableCell>{monitor.type}</TableCell>
                                <TableCell>{monitor.url}</TableCell>
                                <TableCell style={{backgroundColor: monitor.status === "RUNNING" ? "green" : "red"}}>
                                    {monitor.status}
                                </TableCell>
                                <TableCell>
                                    {monitor.status === "RUNNING" ? (
                                        <IconButton onClick={() => handlePauseMonitor(monitor.id)}>
                                            <PauseCircleFilledTwoToneIcon/>
                                        </IconButton>
                                    ) : (
                                        <IconButton onClick={() => handleResumeMonitor(monitor.id)}>
                                            <PlayCircleFilledTwoToneIcon/>
                                        </IconButton>
                                    )}
                                    <IconButton onClick={() => handleOpenDialog(monitor)}>
                                        <EditTwoToneIcon/>
                                    </IconButton>
                                    <IconButton onClick={() => handleDeleteMonitor(monitor.id)}>
                                        <DeleteForeverTwoToneIcon/>
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </Paper>
            <Box sx={{display: "flex", justifyContent: "center", padding: "20px"}}>
                <Button
                    startIcon={<AddCircleOutlineTwoToneIcon/>}
                    variant="contained"
                    color="primary"
                    onClick={() => handleOpenDialog()}
                >
                    Add Monitor
                </Button>
            </Box>
            <MonitorFormDialog
                open={dialogOpen}
                monitor={editingMonitor}
                onSave={handleSaveMonitor}
                onCancel={handleCloseDialog}
            />
        </Container>
    );
};

export default MonitorsDashboard;
