import React, { useEffect, useState } from 'react';
import {
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    Grid,
    TextField,
    Typography,
    Snackbar,
    Alert,
    Switch,
    FormControlLabel,
    IconButton,
    InputAdornment,
    Collapse,
    Tabs,
    Tab,
    MenuItem,
    Select,
    FormControl,
    InputLabel
} from '@mui/material';
import { Edit, Visibility, VisibilityOff, Save } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import { SettingsService } from '../services/SettingsService';
import { BotSettingDTO } from '../types';

interface TabPanelProps {
    children?: React.ReactNode;
    index: number;
    value: number;
}

function CustomTabPanel(props: TabPanelProps) {
    const { children, value, index, ...other } = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`simple-tabpanel-${index}`}
            aria-labelledby={`simple-tab-${index}`}
            {...other}
        >
            {value === index && (
                <Box sx={{ p: 3 }}>
                    {children}
                </Box>
            )}
        </div>
    );
}

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
    const [editDescriptions, setEditDescriptions] = useState<{ [key: string]: string }>({});
    const [showDescriptionEdit, setShowDescriptionEdit] = useState<{ [key: string]: boolean }>({});
    const [showPassword, setShowPassword] = useState<{ [key: string]: boolean }>({});
    const [tabValue, setTabValue] = useState(0);

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
                const initialDescriptions: { [key: string]: string } = {};
                response.data.forEach(s => {
                    initialValues[s.settingType] = s.settingValue || '';
                    initialDescriptions[s.settingType] = s.description || '';
                });
                setEditValues(initialValues);
                setEditDescriptions(initialDescriptions);
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
            const description = editDescriptions[settingType];
            await SettingsService.updateSetting(settingType, value, description);
            setSnackbar({ open: true, message: t('settings.save_success', 'Saved successfully'), severity: 'success' });
            setShowDescriptionEdit(prev => ({ ...prev, [settingType]: false }));
            fetchSettings(); 
        } catch (error) {
            setSnackbar({ open: true, message: t('settings.save_error', 'Error saving'), severity: 'error' });
        }
    };

    const handleChange = (settingType: string, value: string) => {
        setEditValues(prev => ({ ...prev, [settingType]: value }));
    };

    const handleDescriptionChange = (settingType: string, value: string) => {
        setEditDescriptions(prev => ({ ...prev, [settingType]: value }));
    };

    const toggleDescriptionEdit = (settingType: string) => {
        setShowDescriptionEdit(prev => ({ ...prev, [settingType]: !prev[settingType] }));
    };
    
    const togglePasswordVisibility = (settingType: string) => {
        setShowPassword(prev => ({ ...prev, [settingType]: !prev[settingType] }));
    };

    const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
        setTabValue(newValue);
    };

    const handleCloseSnackbar = () => {
        setSnackbar({ ...snackbar, open: false });
    };

    const isSensitive = (key: string) => {
        return key.includes('KEY') || key.includes('SECRET') || key.includes('PASSWORD') || key.includes('TOKEN');
    };

    const renderSettingCard = (setting: BotSettingDTO, overrideInput?: React.ReactNode) => {
        const value = editValues[setting.settingType] || '';
        const description = editDescriptions[setting.settingType] || '';
        const isEditingDesc = showDescriptionEdit[setting.settingType];
        const sensitive = isSensitive(setting.settingType);
        const isVisible = showPassword[setting.settingType];

        return (
            <Card elevation={1} sx={{ mb: 2, borderLeft: '4px solid #90caf9' }}>
                <CardContent sx={{ pb: 1 }}>
                    <Box display="flex" justifyContent="space-between" alignItems="flex-start">
                        <Box flexGrow={1}>
                            <Box display="flex" alignItems="center" gap={1}>
                                <Typography variant="h6" sx={{ fontSize: '1rem', fontWeight: 500 }}>
                                    {description || setting.settingType}
                                </Typography>
                                <IconButton size="small" onClick={() => toggleDescriptionEdit(setting.settingType)}>
                                    <Edit fontSize="small" sx={{ opacity: 0.5 }} />
                                </IconButton>
                            </Box>
                            
                            <Collapse in={isEditingDesc}>
                                <TextField
                                    fullWidth
                                    size="small"
                                    label="Description (Label)"
                                    value={description}
                                    onChange={(e) => handleDescriptionChange(setting.settingType, e.target.value)}
                                    sx={{ mt: 1, mb: 1 }}
                                />
                            </Collapse>
                            
                            {!isEditingDesc && (
                                <Typography variant="caption" display="block" color="text.secondary" sx={{ mb: 1 }}>
                                    Key: {setting.settingType}
                                </Typography>
                            )}
                        </Box>
                        
                        <Button 
                            startIcon={<Save />}
                            onClick={() => handleSave(setting.settingType)}
                            disabled={editValues[setting.settingType] === setting.settingValue && editDescriptions[setting.settingType] === setting.description}
                            size="small"
                        >
                            Save
                        </Button>
                    </Box>

                    {overrideInput ? overrideInput : (
                        setting.valueType === 'boolean' ? (
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
                        ) : (
                            <TextField
                                fullWidth
                                multiline={setting.valueType === 'text' && !sensitive}
                                rows={setting.valueType === 'text' && !sensitive ? 3 : 1}
                                type={setting.valueType === 'number' ? 'number' : (sensitive && !isVisible ? 'password' : 'text')}
                                value={value}
                                onChange={(e) => handleChange(setting.settingType, e.target.value)}
                                variant="outlined"
                                size="small"
                                placeholder={`Enter ${setting.settingType}...`}
                                InputProps={{
                                    endAdornment: sensitive ? (
                                        <InputAdornment position="end">
                                            <IconButton
                                                onClick={() => togglePasswordVisibility(setting.settingType)}
                                                edge="end"
                                            >
                                                {isVisible ? <VisibilityOff /> : <Visibility />}
                                            </IconButton>
                                        </InputAdornment>
                                    ) : null,
                                }}
                            />
                        )
                    )}
                </CardContent>
            </Card>
        );
    };

    if (loading && settings.length === 0) {
        return <Box display="flex" justifyContent="center" p={5}><CircularProgress /></Box>;
    }

    // Filter settings for tabs
    const generalSettings = settings.filter(s => 
        !s.settingType.startsWith('STRIPE_') && 
        !s.settingType.startsWith('CRYPTO_') && 
        s.settingType !== 'STORE_CURRENCY' && 
        s.settingType !== 'MIN_PAYMENT'
    );

    const paymentSettings = settings.filter(s => 
        s.settingType.startsWith('STRIPE_') || 
        s.settingType.startsWith('CRYPTO_') || 
        s.settingType === 'STORE_CURRENCY' || 
        s.settingType === 'MIN_PAYMENT'
    );

    // Crypto Helpers
    const cryptoProviderSetting = paymentSettings.find(s => s.settingType === 'CRYPTO_PROVIDER');
    const cryptoApiKeySetting = paymentSettings.find(s => s.settingType === 'CRYPTO_API_KEY');
    const cryptoEnabledSetting = paymentSettings.find(s => s.settingType === 'CRYPTO_ENABLED');
    const cryptoWebhookSetting = paymentSettings.find(s => s.settingType === 'CRYPTO_WEBHOOK_SECRET');
    const cryptoPayCurrencySetting = paymentSettings.find(s => s.settingType === 'CRYPTO_PAY_CURRENCY');

    // Stripe Helpers
    const stripeSettings = paymentSettings.filter(s => s.settingType.startsWith('STRIPE_'));

    // General Payment Helpers
    const commonPaymentSettings = paymentSettings.filter(s => s.settingType === 'STORE_CURRENCY' || s.settingType === 'MIN_PAYMENT');


    return (
        <Box>
            <Typography variant="h4" component="h1" gutterBottom sx={{ mb: 2 }}>
                {t('settings.title', 'System Configuration')}
            </Typography>

            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={tabValue} onChange={handleTabChange} aria-label="settings tabs">
                    <Tab label="General" />
                    <Tab label="Payment Methods" />
                </Tabs>
            </Box>

            {/* General Settings Tab */}
            <CustomTabPanel value={tabValue} index={0}>
                <Grid container spacing={2}>
                    {generalSettings.map(setting => (
                        <Grid size={{ xs: 12, md: 6 }} key={setting.settingType}>
                            {renderSettingCard(setting)}
                        </Grid>
                    ))}
                </Grid>
            </CustomTabPanel>

            {/* Payment Methods Tab */}
            <CustomTabPanel value={tabValue} index={1}>
                
                {/* 1. Common Payment Settings */}
                <Typography variant="h6" color="primary" sx={{ mt: 2, mb: 2 }}>General Payment Settings</Typography>
                <Grid container spacing={2}>
                    {commonPaymentSettings.map(setting => (
                        <Grid size={{ xs: 12, md: 6 }} key={setting.settingType}>
                            {renderSettingCard(setting)}
                        </Grid>
                    ))}
                </Grid>

                <Typography variant="h6" color="primary" sx={{ mt: 4, mb: 2 }}>Crypto Payments</Typography>
                <Grid container spacing={2}>
                    {/* Crypto Enabled Switch */}
                    {cryptoEnabledSetting && (
                         <Grid size={{ xs: 12 }} key={cryptoEnabledSetting.settingType}>
                             {renderSettingCard(cryptoEnabledSetting)}
                         </Grid>
                    )}

                    {/* Crypto Provider Selector */}
                    {cryptoProviderSetting && (
                        <Grid size={{ xs: 12, md: 6 }} key={cryptoProviderSetting.settingType}>
                            {renderSettingCard(cryptoProviderSetting, (
                                <FormControl fullWidth size="small">
                                    <Select
                                        value={editValues['CRYPTO_PROVIDER'] || ''}
                                        onChange={(e) => handleChange('CRYPTO_PROVIDER', e.target.value)}
                                        displayEmpty
                                    >
                                        <MenuItem value="" disabled>Select Provider</MenuItem>
                                        <MenuItem value="NowPayments">NowPayments</MenuItem>
                                        {/* Future providers can be added here */}
                                    </Select>
                                </FormControl>
                            ))}
                        </Grid>
                    )}

                    {/* Conditional API Key for NowPayments */}
                    {editValues['CRYPTO_PROVIDER'] === 'NowPayments' && cryptoApiKeySetting && (
                        <Grid size={{ xs: 12, md: 6 }} key={cryptoApiKeySetting.settingType}>
                            {renderSettingCard(cryptoApiKeySetting)}
                        </Grid>
                    )}
                     {/* Conditional Webhook Secret for NowPayments */}
                     {editValues['CRYPTO_PROVIDER'] === 'NowPayments' && cryptoWebhookSetting && (
                        <Grid size={{ xs: 12, md: 6 }} key={cryptoWebhookSetting.settingType}>
                            {renderSettingCard(cryptoWebhookSetting)}
                        </Grid>
                    )}
                    {/* Conditional Payout Currency for NowPayments */}
                    {editValues['CRYPTO_PROVIDER'] === 'NowPayments' && cryptoPayCurrencySetting && (
                        <Grid size={{ xs: 12, md: 6 }} key={cryptoPayCurrencySetting.settingType}>
                            {renderSettingCard(cryptoPayCurrencySetting)}
                        </Grid>
                    )}
                </Grid>

                {/* Stripe Section */}
                <Typography variant="h6" color="primary" sx={{ mt: 4, mb: 2 }}>Stripe Payments</Typography>
                <Grid container spacing={2}>
                    {stripeSettings.map(setting => (
                         <Grid size={{ xs: 12, md: 6 }} key={setting.settingType}>
                            {renderSettingCard(setting)}
                        </Grid>
                    ))}
                </Grid>

            </CustomTabPanel>

            <Snackbar open={snackbar.open} autoHideDuration={4000} onClose={handleCloseSnackbar}>
                <Alert onClose={handleCloseSnackbar} severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </Box>
    );
};

export default SettingsPage;
