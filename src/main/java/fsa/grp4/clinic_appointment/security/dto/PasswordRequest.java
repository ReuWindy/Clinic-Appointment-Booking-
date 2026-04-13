package fsa.grp4.clinic_appointment.security.dto;

import fsa.grp4.clinic_appointment.validation.annotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequest {
    private String oldpass;

    @ValidPassword
    private String newpass;
}