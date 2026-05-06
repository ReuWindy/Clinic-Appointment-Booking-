package fsa.grp4.clinic_appointment.repository.contract;

import fsa.grp4.clinic_appointment.entity.OtpToken;
import fsa.grp4.clinic_appointment.repository.IGenericRepository;

import java.util.Optional;

public interface IOtpTokenRepository extends IGenericRepository<OtpToken, Long> {
    Optional<OtpToken> getByEmail(String email);
    void deleteByEmail(String email);
}
