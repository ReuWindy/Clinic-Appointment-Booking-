package fsa.grp4.clinic_appointment.security.dto;

import fsa.grp4.clinic_appointment.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AuthenticatedUserDto {
    int id;
    String username;
    String password;
    Role role;
}
