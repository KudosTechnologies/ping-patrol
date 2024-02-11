import {Monitor, MonitorStatus, MonitorType} from "../utils/PingPatrolApiTypes.ts";
import React, {useEffect, useState} from "react";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    MenuItem,
    Select,
    SelectChangeEvent,
    TextField
} from "@mui/material";


interface MonitorFormDialogProps {
    open: boolean;
    monitor?: Monitor; // If monitor is provided, we're editing, otherwise, we're adding
    onSave: (monitor: Monitor) => void;
    onCancel: () => void;
}

const MonitorFormDialog: React.FC<MonitorFormDialogProps> = ({
                                                                 open,
                                                                 monitor,
                                                                 onSave,
                                                                 onCancel,
                                                             }) => {

    const [formState, setFormState] = useState<Monitor>({
        id: '',
        name: '',
        type: MonitorType.HTTPS, // Default, adjust according to your API types
        url: '',
        status: MonitorStatus.RUNNING, // Default, adjust according to your API types
        monitoringInterval: 60, // Default value
        monitorTimeout: 100, // Default value
    });
    const [errors, setErrors] = useState<{ [key: string]: string }>({});


    useEffect(() => {
        // If we're editing a monitor, populate the form with its data
        if (monitor) {
            setFormState(monitor);
        } else {
            setFormState({
                id: '',
                name: '',
                type: MonitorType.HTTPS, // Default, adjust according to your API types
                url: '',
                status: MonitorStatus.RUNNING, // Default, adjust according to your API types
                monitoringInterval: 60, // Default value
                monitorTimeout: 100, // Default value
            });
        }
    }, [monitor]);


    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setFormState(prevState => ({
            ...prevState,
            [name]: value,
        }));
        validateForm();
    };

    const handleSelectChange = (event: SelectChangeEvent<MonitorType>) => {
        const {name, value} = event.target;
        setFormState(prevState => ({
            ...prevState,
            [name]: value,
        }));
    };

    const validateForm = () => {
        let tempErrors: { [key: string]: string } = {};
        if (!formState.name) tempErrors.name = "Name is required";
        else if (formState.name.length < 3) tempErrors.name = "Name must be at least 3 characters long";
        if (!formState.url) tempErrors.url = "URL is required";
        // Add other validations as needed
        setErrors(tempErrors);
        return Object.keys(tempErrors).length === 0;
    };


    const handleSave = () => {
        if (validateForm()) {
            onSave(formState);
        }
    };

    return (
        <Dialog open={open} onClose={onCancel} aria-labelledby="form-dialog-title">
            <DialogTitle id="form-dialog-title">{monitor ? 'Edit Monitor' : 'Add Monitor'}</DialogTitle>
            <DialogContent>
                <TextField
                    autoFocus
                    margin="dense"
                    id="name"
                    name="name"
                    label="Name"
                    type="text"
                    fullWidth
                    value={formState.name}
                    onChange={handleChange}
                    error={!!errors.name}
                    helperText={errors.name}
                />
                <Select
                    value={formState.type}
                    onChange={handleSelectChange}
                    name="type"
                    id="type"
                    fullWidth
                >
                    {Object.values(MonitorType).map((type) => (
                        <MenuItem key={type} value={type}>
                            {type}
                        </MenuItem>
                    ))}
                </Select>
                <TextField
                    margin="dense"
                    id="url"
                    name="url"
                    label="URL"
                    type="url"
                    fullWidth
                    value={formState.url}
                    onChange={handleChange}
                    error={!!errors.url}
                    helperText={errors.url}
                />
                <TextField
                    margin="dense"
                    id="monitoringInterval"
                    name="monitoringInterval"
                    label="Monitoring Interval (seconds)"
                    type="number"
                    fullWidth
                    value={formState.monitoringInterval}
                    onChange={handleChange}
                />
                <TextField
                    margin="dense"
                    id="monitorTimeout"
                    name="monitorTimeout"
                    label="Monitor Timeout (milliseconds)"
                    type="number"
                    fullWidth
                    value={formState.monitorTimeout}
                    onChange={handleChange}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={onCancel} color="primary">
                    Cancel
                </Button>
                <Button onClick={handleSave} color="primary">
                    Save
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default MonitorFormDialog;