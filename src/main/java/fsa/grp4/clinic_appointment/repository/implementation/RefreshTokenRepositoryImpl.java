package fsa.grp4.clinic_appointment.repository.implementation;

import fsa.grp4.clinic_appointment.entity.RefreshToken;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.repository.GenericRepositoryImpl;
import fsa.grp4.clinic_appointment.repository.contract.IRefreshTokenRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class RefreshTokenRepositoryImpl
                extends GenericRepositoryImpl<RefreshToken, Integer>
                implements IRefreshTokenRepository {

        public RefreshTokenRepositoryImpl() {
                super(RefreshToken.class);
        }

        @Override
        public Optional<RefreshToken> getByTokenHash(String tokenHash) {
                try {
                        String jpql = "SELECT r FROM RefreshToken r " +
                                        "JOIN FETCH r.user " +
                                        "WHERE r.tokenHash = :tokenHash";

                        RefreshToken refreshToken = em.createQuery(jpql, RefreshToken.class)
                                        .setParameter("tokenHash", tokenHash)
                                        .getSingleResult();

                        return Optional.of(refreshToken);
                } catch (NoResultException ex) {
                        return Optional.empty();
                }
        }

        @Override
        public List<RefreshToken> getByUser(User user) {
                String jpql = "SELECT r FROM RefreshToken r " +
                                "WHERE r.user = :user " +
                                "ORDER BY r.createdAt DESC";

                return em.createQuery(jpql, RefreshToken.class)
                                .setParameter("user", user)
                                .getResultList();
        }

        @Override
        public List<RefreshToken> getActiveTokensByUser(User user) {
                String jpql = "SELECT r FROM RefreshToken r " +
                                "WHERE r.user = :user " +
                                "AND r.revoked = false " +
                                "AND r.expiryDate > :now " +
                                "ORDER BY r.createdAt DESC";

                return em.createQuery(jpql, RefreshToken.class)
                                .setParameter("user", user)
                                .setParameter("now", LocalDateTime.now())
                                .getResultList();
        }

        @Override
        public void revokeByTokenHash(String tokenHash) {
                String jpql = "UPDATE RefreshToken r " +
                                "SET r.revoked = true " +
                                "WHERE r.tokenHash = :tokenHash";

                em.createQuery(jpql)
                                .setParameter("tokenHash", tokenHash)
                                .executeUpdate();
        }

        @Override
        public void revokeAllByUser(User user) {
                String jpql = "UPDATE RefreshToken r " +
                                "SET r.revoked = true " +
                                "WHERE r.user = :user AND r.revoked = false";

                em.createQuery(jpql)
                                .setParameter("user", user)
                                .executeUpdate();
        }

        @Override
        public void deleteExpiredTokens(LocalDateTime now) {
                String jpql = "DELETE FROM RefreshToken r " +
                                "WHERE r.expiryDate <= :now OR r.revoked = true";

                em.createQuery(jpql)
                                .setParameter("now", now)
                                .executeUpdate();
        }
}
