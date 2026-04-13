package fsa.grp4.clinic_appointment.security.service;


import fsa.grp4.clinic_appointment.entity.RefreshToken;
import fsa.grp4.clinic_appointment.entity.User;

public interface RefreshTokenService {

    RefreshToken create(User user);

    RefreshToken verify(String refreshToken);

    RefreshToken rotate(RefreshToken oldToken);

    void revoke(String refreshToken);
}
