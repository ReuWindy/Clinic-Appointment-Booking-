package fsa.grp4.clinic_appointment.security.service;

import fsa.grp4.clinic_appointment.dao.contract.IRefreshTokenDAO;
import fsa.grp4.clinic_appointment.entity.RefreshToken;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.exception.NotFoundException;
import fsa.grp4.clinic_appointment.security.utils.TokenHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final IRefreshTokenDAO refreshTokenDAO;

    private static final long REFRESH_TOKEN_DAYS = 7;

    @Override
    public RefreshToken create(User user) {

        String rawToken = UUID.randomUUID().toString();
        String tokenHash = TokenHashUtil.hashToken(rawToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(tokenHash)
                .user(user)
                .revoked(false)
                .expiryDate(LocalDateTime.now().plusDays(REFRESH_TOKEN_DAYS))
                .build();

        RefreshToken saved = refreshTokenDAO.add(refreshToken);

        saved.setRawToken(rawToken);

        return saved;
    }

    @Override
    public RefreshToken verify(String refreshToken) {

        String hash = TokenHashUtil.hashToken(refreshToken);

        RefreshToken token = refreshTokenDAO.getByTokenHash(hash)
                .orElseThrow(() -> new NotFoundException("Refresh token not found"));

        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

    @Override
    public RefreshToken rotate(RefreshToken oldToken) {

        oldToken.setRevoked(true);
        refreshTokenDAO.update(oldToken);

        return create(oldToken.getUser());
    }

    @Override
    public void revoke(String refreshToken) {

        String hash = TokenHashUtil.hashToken(refreshToken);

        RefreshToken token = refreshTokenDAO.getByTokenHash(hash)
                .orElseThrow(() -> new NotFoundException("Refresh token not found"));

        token.setRevoked(true);
        refreshTokenDAO.update(token);
    }
}
