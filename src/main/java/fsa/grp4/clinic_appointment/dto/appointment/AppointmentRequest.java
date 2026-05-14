package fsa.grp4.clinic_appointment.dto.appointment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
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
    Integer patientId;
    String patientName;

    @NotNull(message = "Doctor ID is required")
    @Min(value = 1, message = "Please select a valid doctor")
    Integer doctorId;

    @NotNull(message = "Appointment date is required")
    LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    LocalTime appointmentTime;

    String reason;
}
