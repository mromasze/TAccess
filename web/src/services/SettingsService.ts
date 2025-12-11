import { ApiResponse, BotSettingDTO } from '../types';
import { AuthService } from './AuthService';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const getAuthHeader = (): Record<string, string> => {
    const token = localStorage.getItem('token');
    return token ? { 'Authorization': `Bearer ${token}` } : {};
};

export const SettingsService = {
  getAllSettings: async (): Promise<ApiResponse<BotSettingDTO[]>> => {
    try {
      const headers: Record<string, string> = {
          ...getAuthHeader()
      };
      const response = await fetch(`${API_URL}/settings`, { headers });
      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Failed to fetch settings', error);
      throw error;
    }
  },

    updateSetting: async (settingType: string, value: string, description?: string): Promise<ApiResponse<BotSettingDTO>> => {
        const response = await fetch(`${API_URL}/settings/${settingType}`, {
            method: 'PUT',
            headers: AuthService.authHeader(),
            body: JSON.stringify({ value, description }),
        });
        return handleResponse(response);
    },
};

async function handleResponse(response: Response) {
    const text = await response.text();
    const data = text && JSON.parse(text);
    
    if (!response.ok) {
        const error = (data && data.message) || response.statusText;
        return Promise.reject(error);
    }

    return data;
}
