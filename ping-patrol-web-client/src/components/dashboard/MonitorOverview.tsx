import {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import {pingPatrolApi} from '../../utils/PingPatrolApi';
import {useKeycloak} from '@react-keycloak/web';
import {MonitorRun} from "../../utils/PingPatrolApiTypes.ts";
import {Container, LinearProgress, Paper, Typography} from '@mui/material';
import {LineChart} from "@mui/x-charts";

const MonitorOverview = () => {
    const {monitorId} = useParams();
    const {keycloak} = useKeycloak();
    const [monitorRuns, setMonitorRuns] = useState<MonitorRun[]>([]);
    const [uptimePercentage, setUptimePercentage] = useState(100);

    useEffect(() => {
        if (keycloak.token && monitorId) {
            pingPatrolApi.getMonitorRuns(keycloak.token, monitorId)
                .then(data => {
                    setMonitorRuns(data);
                    calculateUptime(data);
                    console.log("Monitor runs: ", data)
                })
                .catch(error => console.error(error));
        }
    }, [keycloak, monitorId]);

    const calculateUptime = (runs: MonitorRun[]) => {
        const totalRuns = runs.length;
        const reachableRuns = runs.filter(run => run.status === 'REACHABLE').length;
        setUptimePercentage((reachableRuns / totalRuns) * 100);
    };

    const averageResponseTime = monitorRuns.reduce((acc, curr) => acc + curr.duration, 0) / monitorRuns.length || 0;

    const uData = monitorRuns.map(run => run.duration);
    const xLabels = monitorRuns.map(run => new Date(run.startedAt).toLocaleTimeString());


    return (
        <Container sx={{mt: 4}}>
            <Typography variant="h4" gutterBottom>Monitor Overview</Typography>
            <Paper elevation={3} sx={{p: 2, mb: 3}}>
                <Typography variant="h6" gutterBottom>Uptime Percentage</Typography>
                <LinearProgress variant="determinate" value={uptimePercentage} color="success"
                                sx={{
                                    height: 20,
                                    backgroundColor: '#ae0300'
                                }}/>
                <Typography>{`Uptime: ${uptimePercentage.toFixed(2)}%`}</Typography>
            </Paper>
            <Paper elevation={3} sx={{p: 2, mb: 3}}>
                <Typography variant="h6" gutterBottom>Average Response Time</Typography>
                <Typography>{`Average Response Time: ${averageResponseTime.toFixed(2)}ms`}</Typography>
            </Paper>

            <Paper elevation={3} sx={{p: 2, mb: 3}}>
                <Typography variant="h6" gutterBottom>Response Time Chart</Typography>
                <LineChart
                    width={1100}
                    height={300}
                    series={[{data: uData, label: 'Milliseconds', area: true, showMark: true}]}
                    xAxis={[{scaleType: 'point', data: xLabels}]}
                    sx={{
                        '.MuiLineElement-root': {
                            display: 'none',
                        },
                    }}
                />
            </Paper>


        </Container>


    );
};

export default MonitorOverview;