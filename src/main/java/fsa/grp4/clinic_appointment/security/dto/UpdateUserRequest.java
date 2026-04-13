package fsa.grp4.clinic_appointment.security.dto;

import fsa.grp4.clinic_appointment.validation.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {

    @NotBlank(message = "Username is required")
    private String username;

    private String firstName;
    private String lastName;

    private boolean gender;

    @ValidEmail
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Boolean active;

    private Set<UUID> roleIds;
}
