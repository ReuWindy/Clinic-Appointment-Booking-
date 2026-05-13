package fsa.grp4.clinic_appointment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "specialties")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Specialty extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    int id;

    @Column(nullable = false, unique = true, length = 100)
    String name;

    @Column(columnDefinition = "text")
    String description;

    @Builder.Default
    @jakarta.persistence.OneToMany(mappedBy = "specialty", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    java.util.List<Doctor> doctors = new java.util.ArrayList<>();
}
