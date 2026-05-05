package fsa.grp4.clinic_appointment.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctors")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Doctor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speciality_id", referencedColumnName = "id", nullable = false)
    Speciality speciality;

    @Column(nullable = false, name = "experience")
    int experience;
}
