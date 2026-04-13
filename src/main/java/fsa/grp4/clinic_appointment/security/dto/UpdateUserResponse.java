package fsa.grp4.clinic_appointment.security.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserResponse {

    private int id;
    private String username;

    private String firstName;
    private String lastName;

    private boolean gender;
    private String email;
    private String phoneNumber;

    private LocalDate dateOfBirth;
    private boolean active;

    private LocalDateTime updatedAt;

    private Set<String> roles;
}