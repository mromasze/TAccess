import React, { useEffect, useState } from 'react';
import {
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    IconButton,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TextField,
    Typography,
    MenuItem,
    Chip,
    CircularProgress
} from '@mui/material';
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import { Product, ProductService } from '../services/ProductService';

const ProductsPage: React.FC = () => {
    const { t } = useTranslation();
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(false);
    const [openDialog, setOpenDialog] = useState(false);
    const [currentProduct, setCurrentProduct] = useState<Product | null>(null);

    // Form state
    const [formData, setFormData] = useState<Product>({
        name: '',
        price: 0,
        type: 'CHANNEL_ACCESS',
        active: true,
        channelId: '',
        durationDays: 30,
        content: ''
    });

    const fetchProducts = async () => {
        setLoading(true);
        try {
            const response = await ProductService.getAllProducts();
            if (response.success && response.data) {
                setProducts(response.data);
            }
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchProducts();
    }, []);

    const handleOpenDialog = (product?: Product) => {
        if (product) {
            setCurrentProduct(product);
            setFormData(product);
        } else {
            setCurrentProduct(null);
            setFormData({
                name: '',
                price: 0,
                type: 'CHANNEL_ACCESS',
                active: true,
                channelId: '',
                durationDays: 30,
                content: ''
            });
        }
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setCurrentProduct(null);
    };

    const handleDelete = async (id: number) => {
        if (window.confirm(t('products.confirm_delete'))) {
            try {
                await ProductService.deleteProduct(id);
                fetchProducts();
            } catch (error) {
                console.error(error);
            }
        }
    };

    const handleSave = async () => {
        try {
            if (currentProduct && currentProduct.id) {
                await ProductService.updateProduct(currentProduct.id, formData);
            } else {
                await ProductService.createProduct(formData);
            }
            fetchProducts();
            handleCloseDialog();
        } catch (error) {
            console.error(error);
        }
    };

    const handleChange = (field: keyof Product, value: any) => {
        setFormData(prev => ({ ...prev, [field]: value }));
    };

    return (
        <Box>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
                <Typography variant="h4" component="h1">
                    {t('products.title')}
                </Typography>
                <Button
                    variant="contained"
                    startIcon={<AddIcon />}
                    onClick={() => handleOpenDialog()}
                >
                    {t('products.add_new')}
                </Button>
            </Box>

            {loading ? (
                <CircularProgress />
            ) : (
                <TableContainer component={Paper} elevation={3}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>{t('products.name')}</TableCell>
                                <TableCell>{t('products.price')}</TableCell>
                                <TableCell>{t('products.type')}</TableCell>
                                <TableCell>{t('products.active')}</TableCell>
                                <TableCell align="right">Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {products.map((product) => (
                                <TableRow key={product.id}>
                                    <TableCell>{product.name}</TableCell>
                                    <TableCell>{product.price}</TableCell>
                                    <TableCell>
                                        <Chip label={t(`products.types.${product.type}`)} size="small" color="primary" variant="outlined" />
                                    </TableCell>
                                    <TableCell>
                                        <Chip
                                            label={product.active ? 'Active' : 'Inactive'}
                                            color={product.active ? 'success' : 'default'}
                                            size="small"
                                        />
                                    </TableCell>
                                    <TableCell align="right">
                                        <IconButton onClick={() => handleOpenDialog(product)} color="primary">
                                            <EditIcon />
                                        </IconButton>
                                        <IconButton onClick={() => product.id && handleDelete(product.id)} color="error">
                                            <DeleteIcon />
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}

            <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
                <DialogTitle>{currentProduct ? t('products.edit') : t('products.add_new')}</DialogTitle>
                <DialogContent>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
                        <TextField
                            label={t('products.name')}
                            value={formData.name}
                            onChange={(e) => handleChange('name', e.target.value)}
                            fullWidth
                        />
                        <TextField
                            label={t('products.price')}
                            type="number"
                            value={formData.price}
                            onChange={(e) => handleChange('price', parseFloat(e.target.value))}
                            fullWidth
                        />
                        <TextField
                            select
                            label={t('products.type')}
                            value={formData.type}
                            onChange={(e) => handleChange('type', e.target.value)}
                            fullWidth
                        >
                            <MenuItem value="CHANNEL_ACCESS">{t('products.types.CHANNEL_ACCESS')}</MenuItem>
                            <MenuItem value="TEXT">{t('products.types.TEXT')}</MenuItem>
                        </TextField>

                        {formData.type === 'CHANNEL_ACCESS' && (
                            <>
                                <TextField
                                    label={t('products.channel_id')}
                                    value={formData.channelId || ''}
                                    onChange={(e) => handleChange('channelId', e.target.value)}
                                    fullWidth
                                    helperText="Telegram Channel ID"
                                />
                                <TextField
                                    label={t('products.duration')}
                                    type="number"
                                    value={formData.durationDays || 30}
                                    onChange={(e) => handleChange('durationDays', parseInt(e.target.value))}
                                    fullWidth
                                />
                            </>
                        )}

                        {formData.type === 'TEXT' && (
                            <TextField
                                label={t('products.content')}
                                value={formData.content || ''}
                                onChange={(e) => handleChange('content', e.target.value)}
                                multiline
                                rows={4}
                                fullWidth
                            />
                        )}
                        
                        <TextField
                            select
                            label={t('products.active')}
                            value={formData.active ? 'true' : 'false'}
                            onChange={(e) => handleChange('active', e.target.value === 'true')}
                            fullWidth
                        >
                             <MenuItem value="true">Yes</MenuItem>
                             <MenuItem value="false">No</MenuItem>
                        </TextField>
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog}>{t('products.cancel')}</Button>
                    <Button onClick={handleSave} variant="contained">{t('products.save')}</Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default ProductsPage;
