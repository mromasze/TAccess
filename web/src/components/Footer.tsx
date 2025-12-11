import React from 'react';
import { Box, Typography, Link, useTheme } from '@mui/material';
import { useTranslation } from 'react-i18next';

const Footer: React.FC = () => {
    const { t } = useTranslation();

    return (
        <Box
            component="footer"
            sx={{
                py: 3,
                px: 2,
                mt: 'auto',
                backgroundColor: (theme) =>
                    theme.palette.mode === 'light'
                        ? theme.palette.grey[200]
                        : theme.palette.grey[800],
                textAlign: 'center'
            }}
        >
            <Typography variant="body2" color="text.secondary">
                {t('footer.powered_by')} {' '}
                <Link color="inherit" href="https://github.com/mromasze/TAccess">
                    TAccess
                </Link>
            </Typography>
        </Box>
    );
};

export default Footer;
