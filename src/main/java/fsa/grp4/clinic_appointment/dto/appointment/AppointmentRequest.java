package fsa.grp4.clinic_appointment.dto.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentRequest {
    int patientId;
    String patientName;

    @NotNull(message = "Doctor ID is required")
    int doctorId;

    @NotNull(message = "Appointment date is required")
    LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    LocalTime appointmentTime;

    String reason;
}
