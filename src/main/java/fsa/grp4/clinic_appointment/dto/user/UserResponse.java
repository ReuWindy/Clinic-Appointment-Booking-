package fsa.grp4.clinic_appointment.dto.user;

import java.time.LocalDateTime;

import fsa.grp4.clinic_appointment.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String phone;
    private String address;

    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Role role;
}
