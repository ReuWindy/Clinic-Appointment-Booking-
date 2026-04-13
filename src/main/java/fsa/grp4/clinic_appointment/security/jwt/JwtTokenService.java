package fsa.grp4.clinic_appointment.security.jwt;

import fsa.grp4.clinic_appointment.entity.RefreshToken;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.exception.NotFoundException;
import fsa.grp4.clinic_appointment.repository.contract.IUserRepository;
import fsa.grp4.clinic_appointment.security.dto.LoginRequest;
import fsa.grp4.clinic_appointment.security.dto.LoginResponse;
import fsa.grp4.clinic_appointment.security.dto.LogoutResponse;
import fsa.grp4.clinic_appointment.security.dto.RefreshTokenRequest;
import fsa.grp4.clinic_appointment.security.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse login(LoginRequest loginRequest) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Wrong username or password!");
        }

        User user = userRepository.getByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getIsActive()) {
            throw new RuntimeException("User is inactive");
        }

        String accessToken = jwtTokenManager.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getTokenHash(),
                user.getUsername(),
                user.getId()
        );
    }

    public LoginResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken oldToken = refreshTokenService.verify(request.getRefreshToken());

        String newAccessToken = jwtTokenManager.generateAccessToken(oldToken.getUser());
        RefreshToken newRefreshToken = refreshTokenService.rotate(oldToken);

        return new LoginResponse(
                newAccessToken,
                newRefreshToken.getTokenHash(),
                oldToken.getUser().getUsername(),
                oldToken.getUser().getId()
        );
    }

    public LogoutResponse logout(RefreshTokenRequest request) {

        refreshTokenService.revoke(request.getRefreshToken());

        return new LogoutResponse("Logout successfully");
    }
}