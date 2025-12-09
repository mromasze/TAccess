import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
    Box, 
    Button, 
    Card, 
    CardContent, 
    Container, 
    TextField, 
    Typography, 
    Alert 
} from '@mui/material';
import { AuthService } from '../services/AuthService';

const Login: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(''); // Clear previous errors
        
        const response = await AuthService.login({ username, password });
        
        if (response.success && response.data) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', response.data.username);
            navigate('/');
        } else {
            setError(response.message || 'Invalid credentials');
        }
    };

    return (
        <Box
            sx={{
                minHeight: '100vh',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                bgcolor: 'grey.100'
            }}
        >
            <Container maxWidth="xs">
                <Card elevation={5}>
                    <CardContent sx={{ p: 4 }}>
                        <Typography variant="h4" component="h1" align="center" gutterBottom color="primary">
                            TAccess
                        </Typography>
                        <Typography variant="subtitle1" align="center" gutterBottom>
                            Admin Login
                        </Typography>
                        
                        <Box component="form" onSubmit={handleLogin} sx={{ mt: 2 }}>
                            <TextField
                                label="Username"
                                fullWidth
                                margin="normal"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                                autoFocus
                            />
                            <TextField
                                label="Password"
                                type="password"
                                fullWidth
                                margin="normal"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                            
                            {error && (
                                <Alert severity="error" sx={{ mt: 2 }}>
                                    {error}
                                </Alert>
                            )}
                            
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                size="large"
                                sx={{ mt: 3 }}
                            >
                                Login
                            </Button>
                        </Box>
                    </CardContent>
                </Card>
            </Container>
        </Box>
    );
};

export default Login;