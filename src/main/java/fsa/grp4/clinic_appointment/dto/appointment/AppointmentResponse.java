package fsa.grp4.clinic_appointment.dto.appointment;

import fsa.grp4.clinic_appointment.entity.AppointmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentResponse {
    int id;
    int patientId;
    int doctorId;
    String patientName;
    String doctorName;
    String specialtyName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate appointmentDate;
    @JsonFormat(pattern = "HH:mm")
    LocalTime appointmentTime;
    String reason;
    AppointmentStatus status;
}
