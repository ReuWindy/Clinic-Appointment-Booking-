package fsa.grp4.clinic_appointment.dto.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import fsa.grp4.clinic_appointment.entity.AppointmentStatus;
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
    String diagnosis;
    AppointmentStatus status;
}
