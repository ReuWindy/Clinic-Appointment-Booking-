package fsa.grp4.clinic_appointment.dto.appointment;

import java.time.LocalTime;

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
public class DoctorAppointmentResponse {
    int id;
    String patientName;
    String serviceType;
    String diagnosis;
    LocalTime time;
    String status;
}