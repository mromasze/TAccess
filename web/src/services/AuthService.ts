import { ApiResponse, LoginRequest, LoginResponse } from '../types';

const API_URL = '/api/auth';

export const AuthService = {
  login: async (credentials: LoginRequest): Promise<ApiResponse<LoginResponse>> => {
    try {
      const response = await fetch(`${API_URL}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
      });

      // Check if the response is JSON
      const contentType = response.headers.get("content-type");
      if (contentType && contentType.includes("application/json")) {
        const data = await response.json();
        return data;
      } else {
        // Handle non-JSON response (e.g., 404/500 HTML from proxy or server)
        console.error('Received non-JSON response from server');
        return {
          success: false,
          message: 'Server error: Invalid response format',
          data: null
        };
      }
    } catch (error) {
      console.error('Login error', error);
      // Return a proper error response structure
      return {
        success: false,
        message: 'Network error or server unavailable',
        data: null
      };
    }
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser: () => {
    return localStorage.getItem('user');
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('token');
  }
};
