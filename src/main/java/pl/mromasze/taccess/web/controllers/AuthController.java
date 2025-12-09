package pl.mromasze.taccess.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mromasze.taccess.web.dto.ApiResponse;
import pl.mromasze.taccess.web.dto.LoginRequest;
import pl.mromasze.taccess.web.dto.LoginResponse;
import pl.mromasze.taccess.web.services.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        try {
            boolean isValid = authService.validateCredentials(request.getUsername(), request.getPassword());

            if (isValid) {
                // For now, we'll return a simple token (in production, use JWT)
                String token = "simple-token-" + System.currentTimeMillis();
                LoginResponse response = new LoginResponse(token, request.getUsername(), "Login successful");
                return ResponseEntity.ok(ApiResponse.success("Login successful", response));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Invalid username or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred during login"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestHeader(value = "Authorization", required = false) String token) {
        // Simple validation for now - in production, validate JWT token
        if (token != null && token.startsWith("simple-token-")) {
            return ResponseEntity.ok(ApiResponse.success(true));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid or missing token"));
    }
}
