// CustomPieChart.js or CustomPieChart.tsx if you are using TypeScript
import {PieChart} from "@mui/x-charts";
import {styled, Theme, useTheme} from '@mui/material/styles';
import {useDrawingArea} from '@mui/x-charts/hooks';
import React from "react";

const pieParams = {height: 200, margin: {right: 5}};

interface StyledTextProps {
    theme: Theme;
}

interface PieCenterLabelProps {
    children: React.ReactNode;
}

interface DataItem {
    value: number;
    color: string;
}

interface CustomPieChartProps {
    data: DataItem[];
    centerLabel: string;
}

const StyledText = styled('text')<StyledTextProps>(({theme}) => ({
    fill: theme.palette.text.primary,
    textAnchor: 'middle',
    dominantBaseline: 'central',
    fontSize: 20,
}));

const PieCenterLabel: React.FC<PieCenterLabelProps> = ({children}) => {
    const {width, height, left, top} = useDrawingArea();
    const theme = useTheme();
    return (
        <StyledText theme={theme} x={left + width / 2} y={top + height / 2}>
            {children}
        </StyledText>
    );
}

const CustomPieChart: React.FC<CustomPieChartProps> = ({data, centerLabel}) => (

    <PieChart
        series={[
            {innerRadius: 60, data: data},
        ]}
        {...pieParams}
    >
        <PieCenterLabel>{centerLabel}</PieCenterLabel>
    </PieChart>
);

export default CustomPieChart;
