package fsa.grp4.clinic_appointment.security.google;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleUserInfo {
    private String googleId;
    private String email;
    private String fullName;
    private String avatarUrl;
}
