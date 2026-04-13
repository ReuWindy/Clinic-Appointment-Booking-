package fsa.grp4.clinic_appointment.dto.user;

import fsa.grp4.clinic_appointment.entity.Role;
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

    private int id;
    private String username;

    private String fullName;

    private boolean gender;
    private String email;
    private String phoneNumber;

    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Role role;
}
