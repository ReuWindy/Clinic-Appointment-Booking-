package fsa.grp4.clinic_appointment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(nullable = false, name = "full_name", length = 100)
    String fullName;

    @Column(nullable = false, name = "user_name", length = 100)
    String username;

    @Column(name = "password_hash")
    String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "auth_provider")
    AuthProvider authProvider = AuthProvider.LOCAL;

    @Column(nullable = false, unique = true, name = "email", length = 150)
    String email;

    @Column(name = "phone", length = 20)
    String phone;

    @Column(name = "address")
    String address;

    @Column(name = "gender")
    boolean gender;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role", length = 20)
    Role role = Role.PATIENT;

    @PrePersist
    void setDefaults() {
        if (authProvider == null) {
            authProvider = AuthProvider.LOCAL;
        }
        if (role == null) {
            role = Role.PATIENT;
        }
        if (getIsActive() == null) {
            setIsActive(true);
        }
    }
}
