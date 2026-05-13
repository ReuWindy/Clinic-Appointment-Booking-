package fsa.grp4.clinic_appointment.dto.appointment;

import fsa.grp4.clinic_appointment.entity.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentStatusRequest {
    @NotNull(message = "Status is required")
    AppointmentStatus status;
}
