package fsa.grp4.clinic_appointment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "appointments",
        indexes = {
                @Index(name = "idx_appointments_patient_id", columnList = "patient_id"),
                @Index(name = "idx_appointments_doctor_id", columnList = "doctor_id"),
                @Index(name = "idx_appointments_date", columnList = "appointment_date")
        }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    User patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Doctor doctor;

    @Column(name = "appointment_date", nullable = false)
    LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    LocalTime appointmentTime;

    @Column(columnDefinition = "text")
    String reason;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    AppointmentStatus status = AppointmentStatus.PENDING;

    @PrePersist
    void setDefaults() {
        if (status == null) {
            status = AppointmentStatus.PENDING;
        }
        if (getIsActive() == null) {
            setIsActive(true);
        }
    }
}
