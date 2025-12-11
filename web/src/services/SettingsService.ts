import { ApiResponse, BotSettingDTO, UpdateSettingRequest } from '../types';

const API_URL = '/api/settings';

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
      const response = await fetch(API_URL, { headers });
      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Failed to fetch settings', error);
      throw error;
    }
  },

  updateSetting: async (settingType: string, value: string): Promise<ApiResponse<BotSettingDTO>> => {
    try {
      const request: UpdateSettingRequest = { value, modifiedBy: 'admin' };
      const headers: Record<string, string> = {
          'Content-Type': 'application/json',
          ...getAuthHeader()
      };
      const response = await fetch(`${API_URL}/${settingType}`, {
        method: 'PUT',
        headers,
        body: JSON.stringify(request),
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error(`Failed to update setting ${settingType}`, error);
      throw error;
    }
  }
};
