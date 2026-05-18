package fsa.grp4.clinic_appointment.dto.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorMeResponse {
    int doctorId;
    int userId;
    String fullName;
}