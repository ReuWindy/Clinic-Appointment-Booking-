package fsa.grp4.clinic_appointment.dto.patient;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorPatientListResponse {
    LocalTime time;
    String patientName;
    String gender;
    int age; //fake
    String status;
}