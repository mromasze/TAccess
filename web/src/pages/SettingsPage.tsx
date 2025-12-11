import React, { useEffect, useState } from 'react';
import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    CircularProgress,
    Grid,
    TextField,
    Typography,
    Snackbar,
    Alert,
    Switch,
    FormControlLabel
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { SettingsService } from '../services/SettingsService';
import { BotSettingDTO } from '../types';

const SettingsPage: React.FC = () => {
    const { t } = useTranslation();
    const [settings, setSettings] = useState<BotSettingDTO[]>([]);
    const [loading, setLoading] = useState(false);
    const [snackbar, setSnackbar] = useState<{ open: boolean, message: string, severity: 'success' | 'error' }>({
        open: false,
        message: '',
        severity: 'success'
    });

    const [editValues, setEditValues] = useState<{ [key: string]: string }>({});

    useEffect(() => {
        fetchSettings();
    }, []);

    const fetchSettings = async () => {
        setLoading(true);
        try {
            const response = await SettingsService.getAllSettings();
            if (response.success && response.data) {
                setSettings(response.data);
                const initialValues: { [key: string]: string } = {};
                response.data.forEach(s => {
                    initialValues[s.settingType] = s.settingValue;
                });
                setEditValues(initialValues);
            }
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleSave = async (settingType: string) => {
        try {
            const value = editValues[settingType];
            await SettingsService.updateSetting(settingType, value);
            setSnackbar({ open: true, message: t('settings.save_success'), severity: 'success' });
            fetchSettings(); // Refresh
        } catch (error) {
            setSnackbar({ open: true, message: t('settings.save_error'), severity: 'error' });
        }
    };

    const handleChange = (settingType: string, value: string) => {
        setEditValues(prev => ({ ...prev, [settingType]: value }));
    };

    const handleCloseSnackbar = () => {
        setSnackbar({ ...snackbar, open: false });
    };

    const renderInput = (setting: BotSettingDTO) => {
        const value = editValues[setting.settingType] || '';

        if (setting.valueType === 'boolean') {
            return (
                <FormControlLabel
                    control={
                        <Switch
                            checked={value === 'true'}
                            onChange={(e) => handleChange(setting.settingType, String(e.target.checked))}
                            color="primary"
                        />
                    }
                    label={value === 'true' ? 'Enabled' : 'Disabled'}
                />
            );
        }

        return (
            <TextField
                fullWidth
                multiline={setting.valueType === 'text'}
                rows={setting.valueType === 'text' ? 4 : 1}
                type={setting.valueType === 'number' ? 'number' : 'text'}
                label="Value"
                value={value}
                onChange={(e) => handleChange(setting.settingType, e.target.value)}
                variant="outlined"
                sx={{ mt: 2 }}
            />
        );
    };

    if (loading && settings.length === 0) {
        return <CircularProgress />;
    }

    return (
        <Box>
            <Typography variant="h4" component="h1" gutterBottom>
                {t('settings.title')}
            </Typography>

            <Grid container spacing={3}>
                {settings.map((setting) => (
                    <Grid size={{ xs: 12, md: 6 }} key={setting.settingType}>
                        <Card elevation={3}>
                            <CardContent>
                                <Typography variant="h6" color="primary" gutterBottom>
                                    {setting.settingType}
                                </Typography>
                                <Typography variant="body2" color="text.secondary" gutterBottom>
                                    {setting.description || 'No description'}
                                </Typography>
                                
                                {renderInput(setting)}
                            </CardContent>
                            <CardActions sx={{ justifyContent: 'flex-end', p: 2 }}>
                                <Button 
                                    variant="contained" 
                                    onClick={() => handleSave(setting.settingType)}
                                    disabled={editValues[setting.settingType] === setting.settingValue}
                                >
                                    {t('products.save')}
                                </Button>
                            </CardActions>
                        </Card>
                    </Grid>
                ))}
            </Grid>

            <Snackbar open={snackbar.open} autoHideDuration={6000} onClose={handleCloseSnackbar}>
                <Alert onClose={handleCloseSnackbar} severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </Box>
    );
};

export default SettingsPage;
