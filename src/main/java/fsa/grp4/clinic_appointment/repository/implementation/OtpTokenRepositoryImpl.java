package fsa.grp4.clinic_appointment.repository.implementation;

import fsa.grp4.clinic_appointment.entity.OtpToken;
import fsa.grp4.clinic_appointment.repository.GenericRepositoryImpl;
import fsa.grp4.clinic_appointment.repository.contract.IOtpTokenRepository;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class OtpTokenRepositoryImpl extends GenericRepositoryImpl<OtpToken, Long> implements IOtpTokenRepository {
    public OtpTokenRepositoryImpl() {
        super(OtpToken.class);
    }

    @Override
    public Optional<OtpToken> getByEmail(String email) {
        try {
            String jpql = "SELECT o FROM OtpToken o WHERE o.email = :email";
            OtpToken otpToken = em.createQuery(jpql, OtpToken.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(otpToken);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteByEmail(String email) {
        String jpql = "DELETE FROM OtpToken o WHERE o.email = :email";
        em.createQuery(jpql)
                .setParameter("email", email)
                .executeUpdate();
    }
}
