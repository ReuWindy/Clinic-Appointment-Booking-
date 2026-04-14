package fsa.grp4.clinic_appointment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false, name = "full_name")
    String fullName;

    @Column(nullable = false, name = "user_name")
    String username;

    @Column(nullable = false, name = "user_password")
    String password;

    @Column(nullable = false, name = "email")
    String email;

    @Column(nullable = false, name = "phone")
    String phone;

    @Column(nullable = false, name = "address")
    String address;

    @Column(nullable = false, name = "gender")
    boolean gender;

    @Enumerated(EnumType.STRING)
    Role role;

}
