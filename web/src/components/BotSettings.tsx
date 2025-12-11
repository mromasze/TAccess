import React, { useEffect, useState } from 'react';
import { BotSettingDTO } from '../types';
import { SettingsService } from '../services/SettingsService';

const BotSettings: React.FC = () => {
  const [settings, setSettings] = useState<BotSettingDTO[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [editValues, setEditValues] = useState<{ [key: string]: string }>({});

  useEffect(() => {
    fetchSettings();
  }, []);

  const fetchSettings = async () => {
    try {
      const response = await SettingsService.getAllSettings();
      if (response.success && response.data) {
        setSettings(response.data);
        // Initialize edit values
        const initialValues: { [key: string]: string } = {};
        response.data.forEach(s => {
          initialValues[s.settingType] = s.settingValue;
        });
        setEditValues(initialValues);
      } else {
        setError(response.message || 'Failed to load settings');
      }
    } catch (err) {
      setError('An error occurred while fetching settings.');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (settingType: string, value: string) => {
    setEditValues(prev => ({
      ...prev,
      [settingType]: value
    }));
  };

  const handleSave = async (settingType: string) => {
    try {
      const newValue = editValues[settingType];
      const response = await SettingsService.updateSetting(settingType, newValue);
      if (response.success && response.data) {
        alert('Setting updated successfully!');
        // Update the main list with the new value
        setSettings(prev => prev.map(s =>
          s.settingType === settingType ? response.data! : s
        ));
      } else {
        alert(`Failed to update: ${response.message}`);
      }
    } catch (err) {
      alert('Error saving setting.');
    }
  };

  if (loading) return <div>Loading settings...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="settings-container">
      <h2>Bot Settings</h2>
      <table className="settings-table">
        <thead>
          <tr>
            <th>Setting</th>
            <th>Description</th>
            <th>Value</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {settings.map((setting) => (
            <tr key={setting.settingType}>
              <td>{setting.settingType}</td>
              <td>{setting.description}</td>
              <td>
                {renderInput(setting, editValues[setting.settingType], handleInputChange)}
              </td>
              <td>
                <button 
                    onClick={() => handleSave(setting.settingType)}
                    disabled={editValues[setting.settingType] === setting.settingValue}
                >
                  Save
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

const renderInput = (
    setting: BotSettingDTO, 
    currentValue: string, 
    onChange: (type: string, val: string) => void
) => {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    onChange(setting.settingType, e.target.value);
  };

  if (setting.valueType === 'boolean') {
    return (
      <select value={currentValue} onChange={handleChange}>
        <option value="true">True</option>
        <option value="false">False</option>
      </select>
    );
  } else if (setting.valueType === 'number') {
    return (
      <input 
        type="number" 
        value={currentValue} 
        onChange={handleChange} 
      />
    );
  } else {
    // text
    return (
      <textarea 
        value={currentValue} 
        onChange={handleChange}
        rows={3}
      />
    );
  }
};

export default BotSettings;
