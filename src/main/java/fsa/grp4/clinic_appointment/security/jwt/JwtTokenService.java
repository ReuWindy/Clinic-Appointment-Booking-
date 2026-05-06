package fsa.grp4.clinic_appointment.security.jwt;

import fsa.grp4.clinic_appointment.entity.AuthProvider;
import fsa.grp4.clinic_appointment.entity.RefreshToken;
import fsa.grp4.clinic_appointment.entity.Role;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.exception.ConflictException;
import fsa.grp4.clinic_appointment.exception.NotFoundException;
import fsa.grp4.clinic_appointment.repository.contract.IUserRepository;
import fsa.grp4.clinic_appointment.security.dto.GoogleLoginRequest;
import fsa.grp4.clinic_appointment.security.dto.LoginRequest;
import fsa.grp4.clinic_appointment.security.dto.LoginResponse;
import fsa.grp4.clinic_appointment.security.dto.LogoutResponse;
import fsa.grp4.clinic_appointment.security.dto.RefreshTokenRequest;
import fsa.grp4.clinic_appointment.security.google.GoogleTokenVerifier;
import fsa.grp4.clinic_appointment.security.google.GoogleUserInfo;
import fsa.grp4.clinic_appointment.security.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final GoogleTokenVerifier googleTokenVerifier;

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userRepository.getByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getAuthProvider() != null && user.getAuthProvider() != AuthProvider.LOCAL) {
            throw new RuntimeException("Please login with " + user.getAuthProvider());
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Wrong username or password!");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("User is inactive");
        }

        String accessToken = jwtTokenManager.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getRawToken(),
                user.getUsername(),
                user.getId()
        );
    }

    @Transactional
    public LoginResponse loginWithGoogle(GoogleLoginRequest request) {
        GoogleUserInfo googleUser = googleTokenVerifier.verify(request.getIdToken());

        User user = userRepository.getByEmail(googleUser.getEmail())
                .orElseGet(() -> createGoogleUser(googleUser));

        if (user.getAuthProvider() != AuthProvider.GOOGLE) {
            throw new ConflictException("Email is already registered with password login");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("User is inactive");
        }

        String accessToken = jwtTokenManager.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getRawToken(),
                user.getUsername(),
                user.getId()
        );
    }

    private User createGoogleUser(GoogleUserInfo googleUser) {
        String email = googleUser.getEmail();
        String fullName = googleUser.getFullName() == null || googleUser.getFullName().isBlank()
                ? email
                : googleUser.getFullName();

        User user = User.builder()
                .fullName(fullName)
                .username(email)
                .password(null)
                .authProvider(AuthProvider.GOOGLE)
                .email(email)
                .phone("")
                .address("")
                .gender(true)
                .role(Role.PATIENT)
                .build();
        user.setIsActive(true);

        return userRepository.add(user);
    }

    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken oldToken = refreshTokenService.verify(request.getRefreshToken());

        String newAccessToken = jwtTokenManager.generateAccessToken(oldToken.getUser());
        RefreshToken newRefreshToken = refreshTokenService.rotate(oldToken);

        return new LoginResponse(
                newAccessToken,
                newRefreshToken.getRawToken(),
                oldToken.getUser().getUsername(),
                oldToken.getUser().getId()
        );
    }

    @Transactional
    public LogoutResponse logout(RefreshTokenRequest request) {

        refreshTokenService.revoke(request.getRefreshToken());

        return new LogoutResponse("Logout successfully");
    }
}
