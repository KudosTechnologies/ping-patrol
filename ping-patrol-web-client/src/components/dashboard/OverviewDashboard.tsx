import {useKeycloak} from "@react-keycloak/web";
import {useEffect, useState} from "react";
import {Monitor, MonitorStatus} from "../../utils/PingPatrolApiTypes.ts";
import {pingPatrolApi} from "../../utils/PingPatrolApi.ts";
import {handleLogError} from "../../utils/Helpers.ts";
import {Box, Container, Paper, Typography} from "@mui/material";
import PieChartTwoToneIcon from '@mui/icons-material/PieChartTwoTone';
import Stack from '@mui/material/Stack';

import CustomPieChart from "./CustomPieChart.tsx";
import Divider from "@mui/material/Divider";

const OverviewDashboard = () => {
    const {keycloak} = useKeycloak();
    const [monitors, setMonitors] = useState<Monitor[]>([]);
    const [upMonitors, setUpMonitors] = useState<Monitor[]>([]);
    const [downMonitors, setDownMonitors] = useState<Monitor[]>([]);
    const [pausedMonitors, setPausedMonitors] = useState<Monitor[]>([]);

    useEffect(() => {
        handleGetMonitors().then();
    }, []);

    const handleGetMonitors = async () => {
        try {
            const response = await pingPatrolApi.getMonitors(keycloak.token!); // Ensure keycloak.token is not undefined
            setMonitors(response);
            setUpMonitors(response.filter(monitor => monitor.status === MonitorStatus.RUNNING));
            setDownMonitors(response.filter(monitor => monitor.status === MonitorStatus.DOWN));
            setPausedMonitors(response.filter(monitor => monitor.status === MonitorStatus.PAUSED));
            console.log("Monitors: ", response.length)
        } catch (error) {
            handleLogError(error);
        }
    };

    const upMonitorsChartData = [
        {value: upMonitors.length, color: '#2E7D33'},
        {value: monitors.length - upMonitors.length, color: '#808080'},
    ];

    const downMonitorsChartData = [
        {value: downMonitors.length, color: '#BA3737'},
        {value: monitors.length - downMonitors.length, color: '#808080'},
    ];

    const pausedMonitorChartData = [
        {value: pausedMonitors.length, color: '#000000'},
        {value: monitors.length - pausedMonitors.length, color: '#808080'},
    ];

    return (
        <Container>
            <Paper elevation={3} sx={{p: 2, mb: 3}}>
                <Box display="flex" alignItems="center" mt={2} mb={2}>
                    <PieChartTwoToneIcon fontSize="large" sx={{mr: 2}}/>
                    <Typography variant="h4">Overview</Typography>
                </Box>
                <Box display="flex" alignItems="center" mt={2} mb={2}>
                    <Typography variant="h6">You are currently using {monitors.length} monitors</Typography>
                </Box>
                <Divider></Divider>
                <Stack direction="row" width="100%" textAlign="center" spacing={2} mt={2} mb={2}>
                    <Box flexGrow={1}>
                        <Typography variant="h6">UP MONITORS</Typography>
                        <CustomPieChart data={upMonitorsChartData} centerLabel={""+upMonitors.length}/>
                    </Box>
                    <Box flexGrow={1}>
                        <Typography variant="h6">DOWN MONITORS</Typography>
                        <CustomPieChart data={downMonitorsChartData} centerLabel={""+downMonitors.length}/>
                    </Box>
                    <Box flexGrow={1}>
                        <Typography variant="h6">PAUSED MONITORS</Typography>
                        <CustomPieChart data={pausedMonitorChartData} centerLabel={""+pausedMonitors.length}/>
                    </Box>
                </Stack>
            </Paper>
        </Container>
    );
}

export default OverviewDashboard;
