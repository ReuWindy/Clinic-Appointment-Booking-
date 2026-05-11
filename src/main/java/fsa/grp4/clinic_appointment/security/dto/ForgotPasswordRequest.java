package fsa.grp4.clinic_appointment.security.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForgotPasswordRequest {
    private String email;
}
