package fsa.grp4.clinic_appointment.dao.contract;

import fsa.grp4.clinic_appointment.entity.RefreshToken;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.repository.IGenericRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IRefreshTokenDAO extends IGenericRepository<RefreshToken, Integer> {

    Optional<RefreshToken> getByTokenHash(String tokenHash);

    List<RefreshToken> getByUser(User user);

    List<RefreshToken> getActiveTokensByUser(User user);

    void revokeByTokenHash(String tokenHash);

    void revokeAllByUser(User user);

    void deleteExpiredTokens(LocalDateTime now);
}
