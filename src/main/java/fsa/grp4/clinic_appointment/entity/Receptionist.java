package fsa.grp4.clinic_appointment.entity;

import java.math.BigDecimal;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Receptionist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;

    @Column(name = "salary", nullable = false, precision = 10, scale = 2)
    BigDecimal salary;

    @Column(name = "shift", nullable = false)
    String shift;

    @Column(name = "is_active", nullable = false)
    boolean isActive;
}

