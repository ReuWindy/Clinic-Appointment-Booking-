package fsa.grp4.clinic_appointment.dto.user;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private UUID id;
    private String username;

    private String firstName;
    private String lastName;

    private boolean gender;
    private String email;
    private String phoneNumber;

    private LocalDate dateOfBirth;
    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Set<String> roles;
}
