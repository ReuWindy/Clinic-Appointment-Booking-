package fsa.grp4.clinic_appointment.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "specialities")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Speciality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false, name = "name")
    String name;

    @Column(nullable = false, name = "description")
    String description;

    @OneToMany(mappedBy = "speciality")
    List<Doctor> doctors;
}
