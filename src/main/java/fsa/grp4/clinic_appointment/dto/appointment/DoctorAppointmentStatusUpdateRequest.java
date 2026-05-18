package fsa.grp4.clinic_appointment.dto.appointment;

import fsa.grp4.clinic_appointment.entity.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorAppointmentStatusUpdateRequest {
    @NotNull(message = "Status is required")
    AppointmentStatus status;

    @Size(max = 2000, message = "Diagnosis must not exceed 2000 characters")
    String diagnosis;
}
