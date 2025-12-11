package pl.mromasze.taccess.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mromasze.taccess.bot.dto.BotSettingDTO;
import pl.mromasze.taccess.bot.entity.BotSettings;
import pl.mromasze.taccess.bot.enums.SettingType;
import pl.mromasze.taccess.bot.services.BotSettingsService;
import pl.mromasze.taccess.web.dto.ApiResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class SettingsController {

    private final BotSettingsService botSettingsService;

    public SettingsController(BotSettingsService botSettingsService) {
        this.botSettingsService = botSettingsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BotSettingDTO>>> getAllSettings() {
        try {
            List<BotSettings> settings = botSettingsService.getAllSettings();
            List<BotSettingDTO> dtos = settings.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch settings: " + e.getMessage()));
        }
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<SettingTypeInfo>>> getSettingTypes() {
        try {
            List<SettingTypeInfo> types = Arrays.stream(SettingType.values())
                    .map(type -> new SettingTypeInfo(
                            type.name(),
                            type.getKey(),
                            type.getDescription()
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(types));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch setting types: " + e.getMessage()));
        }
    }

    @GetMapping("/{settingType}")
    public ResponseEntity<ApiResponse<BotSettingDTO>> getSetting(@PathVariable String settingType) {
        try {
            SettingType type = SettingType.valueOf(settingType);
            BotSettings settings = botSettingsService.getSetting(type).orElse(null);

            if (settings == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Setting not found"));
            }

            return ResponseEntity.ok(ApiResponse.success(convertToDTO(settings)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid setting type: " + settingType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch setting: " + e.getMessage()));
        }
    }

    @PutMapping("/{settingType}")
    public ResponseEntity<ApiResponse<BotSettingDTO>> updateSetting(
            @PathVariable String settingType,
            @RequestBody UpdateSettingRequest request) {
        try {
            SettingType type = SettingType.valueOf(settingType);
            String modifiedBy = request.getModifiedBy() != null ? request.getModifiedBy() : "admin";

            botSettingsService.updateSetting(type, request.getValue(), request.getDescription(), modifiedBy);

            BotSettings updated = botSettingsService.getSetting(type).orElse(null);
            return ResponseEntity.ok(ApiResponse.success("Setting updated successfully",
                    convertToDTO(updated)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid setting type: " + settingType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating setting: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{settingType}")
    public ResponseEntity<ApiResponse<Void>> deleteSetting(@PathVariable String settingType) {
        try {
            SettingType type = SettingType.valueOf(settingType);
            botSettingsService.deleteSetting(type);
            return ResponseEntity.ok(ApiResponse.success("Setting deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid setting type: " + settingType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting setting: " + e.getMessage()));
        }
    }

    private BotSettingDTO convertToDTO(BotSettings settings) {
        if (settings == null) {
            return null;
        }
        BotSettingDTO dto = new BotSettingDTO();
        dto.setSettingType(settings.getSettingType());
        dto.setSettingValue(settings.getSettingValue());
        dto.setLastModified(settings.getLastModified());
        dto.setModifiedBy(settings.getModifiedBy());
        
        // Use custom description if available, otherwise fallback to enum description
        if (settings.getCustomDescription() != null && !settings.getCustomDescription().isEmpty()) {
            dto.setDescription(settings.getCustomDescription());
        } else {
            dto.setDescription(settings.getSettingType() != null ?
                settings.getSettingType().getDescription() : null);
        }
        
        dto.setValueType(settings.getSettingType() != null ?
                settings.getSettingType().getValueType() : null);
        return dto;
    }

    public static class SettingTypeInfo {
        private String name;
        private String key;
        private String description;

        public SettingTypeInfo(String name, String key, String description) {
            this.name = name;
            this.key = key;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getKey() {
            return key;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class UpdateSettingRequest {
        private String value;
        private String description;
        private String modifiedBy;

        public String getValue() {
            return value;
        }
        
        public String getDescription() {
            return description;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }
    }
}