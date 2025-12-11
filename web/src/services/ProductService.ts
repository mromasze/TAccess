import { ApiResponse } from '../types';

const API_URL = '/api/products';

export interface Product {
    id?: number;
    name: string;
    price: number;
    active?: boolean;
    type: 'CHANNEL_ACCESS' | 'TEXT';
    channelId?: string;
    durationDays?: number;
    content?: string;
}

const getAuthHeader = (): Record<string, string> => {
    const token = localStorage.getItem('token');
    return token ? { 'Authorization': `Bearer ${token}` } : {};
};

export const ProductService = {
    getAllProducts: async (): Promise<ApiResponse<Product[]>> => {
        const headers: Record<string, string> = {
            'Content-Type': 'application/json',
            ...getAuthHeader()
        };
        const response = await fetch(API_URL, { headers });
        if (!response.ok) throw new Error('Failed to fetch products');
        return response.json();
    },

    getProduct: async (id: number): Promise<ApiResponse<Product>> => {
        const headers: Record<string, string> = {
            'Content-Type': 'application/json',
            ...getAuthHeader()
        };
        const response = await fetch(`${API_URL}/${id}`, { headers });
        if (!response.ok) throw new Error('Failed to fetch product');
        return response.json();
    },

    createProduct: async (product: Product): Promise<ApiResponse<Product>> => {
        const headers: Record<string, string> = {
            'Content-Type': 'application/json',
            ...getAuthHeader()
        };
        const response = await fetch(API_URL, {
            method: 'POST',
            headers,
            body: JSON.stringify(product),
        });
        if (!response.ok) throw new Error('Failed to create product');
        return response.json();
    },

    updateProduct: async (id: number, product: Product): Promise<ApiResponse<Product>> => {
        const headers: Record<string, string> = {
            'Content-Type': 'application/json',
            ...getAuthHeader()
        };
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'PUT',
            headers,
            body: JSON.stringify(product),
        });
        if (!response.ok) throw new Error('Failed to update product');
        return response.json();
    },

    deleteProduct: async (id: number): Promise<ApiResponse<void>> => {
        const headers: Record<string, string> = {
            ...getAuthHeader()
        };
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
            headers
        });
        if (!response.ok) throw new Error('Failed to delete product');
        return response.json();
    }
};
