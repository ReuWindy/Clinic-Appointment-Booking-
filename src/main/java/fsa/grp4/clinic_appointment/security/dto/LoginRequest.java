package fsa.grp4.clinic_appointment.security.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}
