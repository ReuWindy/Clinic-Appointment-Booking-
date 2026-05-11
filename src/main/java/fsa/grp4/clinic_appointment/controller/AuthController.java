package fsa.grp4.clinic_appointment.controller;

import fsa.grp4.clinic_appointment.common.ApiSuccessResponse;
import fsa.grp4.clinic_appointment.security.dto.*;
import fsa.grp4.clinic_appointment.security.jwt.JwtTokenService;
import fsa.grp4.clinic_appointment.security.service.AuthService;
import fsa.grp4.clinic_appointment.security.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Duration ACCESS_TOKEN_COOKIE_MAX_AGE = Duration.ofHours(24);
    private static final Duration REFRESH_TOKEN_COOKIE_MAX_AGE = Duration.ofDays(7);

    private final JwtTokenService jwtTokenService;
    private final UserServiceImpl userService;
    @Autowired
    private AuthService authService;
    public AuthController(JwtTokenService jwtTokenService, UserServiceImpl userService) {
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiSuccessResponse<RegistrationResponse>> register(
            @RequestBody @Valid RegistrationRequest request
    ) {

        RegistrationResponse responseData = userService.registration(request);

        ApiSuccessResponse<RegistrationResponse> response =
                ApiSuccessResponse.<RegistrationResponse>builder()
                        .message("Registration successfully")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK.name())
                        .timestamp(LocalDateTime.now())
                        .data(responseData)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<LoginResponse>> login(
            @RequestBody @Valid LoginRequest request
    ) {

        LoginResponse responseData = jwtTokenService.login(request);

        ApiSuccessResponse<LoginResponse> response =
                ApiSuccessResponse.<LoginResponse>builder()
                        .message("Login successfully")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK.name())
                        .timestamp(LocalDateTime.now())
                        .data(responseData)
                        .build();

        return withAuthCookies(responseData, response);
    }

    @PostMapping("/google")
    public ResponseEntity<ApiSuccessResponse<LoginResponse>> googleLogin(
            @RequestBody @Valid GoogleLoginRequest request
    ) {

        LoginResponse responseData = jwtTokenService.loginWithGoogle(request);

        ApiSuccessResponse<LoginResponse> response =
                ApiSuccessResponse.<LoginResponse>builder()
                        .message("Login with Google successfully")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK.name())
                        .timestamp(LocalDateTime.now())
                        .data(responseData)
                        .build();

        return withAuthCookies(responseData, response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiSuccessResponse<LoginResponse>> refresh(
            @RequestBody(required = false) RefreshTokenRequest request,
            @CookieValue(name = "refreshToken", required = false) String refreshTokenCookie
    ) {

        request = new RefreshTokenRequest(resolveRefreshToken(request, refreshTokenCookie));
        LoginResponse responseData = jwtTokenService.refreshToken(request);

        ApiSuccessResponse<LoginResponse> response =
                ApiSuccessResponse.<LoginResponse>builder()
                        .message("Refresh token successfully")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK.name())
                        .timestamp(LocalDateTime.now())
                        .data(responseData)
                        .build();

        return withAuthCookies(responseData, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiSuccessResponse<LogoutResponse>> logout(
            @RequestBody(required = false) RefreshTokenRequest request,
            @CookieValue(name = "refreshToken", required = false) String refreshTokenCookie
    ) {

        request = new RefreshTokenRequest(resolveRefreshToken(request, refreshTokenCookie));
        LogoutResponse responseData = jwtTokenService.logout(request);

        ApiSuccessResponse<LogoutResponse> response =
                ApiSuccessResponse.<LogoutResponse>builder()
                        .message("Logout successfully")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK.name())
                        .timestamp(LocalDateTime.now())
                        .data(responseData)
                        .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, clearCookie("accessToken").toString())
                .header(HttpHeaders.SET_COOKIE, clearCookie("refreshToken").toString())
                .body(response);
    }

    private ResponseEntity<ApiSuccessResponse<LoginResponse>> withAuthCookies(
            LoginResponse responseData,
            ApiSuccessResponse<LoginResponse> response
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, createCookie(
                        "accessToken",
                        responseData.getAccessToken(),
                        ACCESS_TOKEN_COOKIE_MAX_AGE
                ).toString())
                .header(HttpHeaders.SET_COOKIE, createCookie(
                        "refreshToken",
                        responseData.getRefreshToken(),
                        REFRESH_TOKEN_COOKIE_MAX_AGE
                ).toString())
                .body(response);
    }

    private ResponseCookie createCookie(String name, String value, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    private ResponseCookie clearCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
    }

    private String resolveRefreshToken(RefreshTokenRequest request, String refreshTokenCookie) {
        if (request != null && request.getRefreshToken() != null && !request.getRefreshToken().isBlank()) {
            return request.getRefreshToken();
        }

        if (refreshTokenCookie != null && !refreshTokenCookie.isBlank()) {
            return refreshTokenCookie;
        }

        throw new IllegalArgumentException("Refresh token is required");
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            String message = authService.generateAndSendOtp(request.getEmail());
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        try {
            String message = authService.verifyOtp(request);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            String message = authService.resetPassword(request);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
