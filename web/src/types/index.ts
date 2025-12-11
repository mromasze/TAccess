export interface ApiResponse<T> {
  success: boolean;
  message: string | null;
  data: T | null;
}

export interface BotSettingDTO {
  settingType: string;
  settingValue: string;
  lastModified: string;
  modifiedBy: string;
  description: string;
  valueType: 'text' | 'boolean' | 'number';
}

export interface UpdateSettingRequest {
  value: string;
  modifiedBy?: string;
}

export interface LoginRequest {
  username?: string;
  password?: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  message: string;
}
