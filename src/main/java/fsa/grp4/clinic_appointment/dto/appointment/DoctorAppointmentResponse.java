package fsa.grp4.clinic_appointment.dto.appointment;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorAppointmentResponse {
    int id;
    String patientName;
    String serviceType;
    LocalTime time;
    String status;
}