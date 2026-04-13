package fsa.grp4.clinic_appointment.security.dto;

import fsa.grp4.clinic_appointment.validation.annotation.ValidEmail;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RegistrationRequest {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String passwordConfirmation;
    private String phone;
    private boolean active = true;
    @ValidEmail
    @NotEmpty
    private String email;
    private String address;
    private LocalDate dob;
    private boolean gender;
}
