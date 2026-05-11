package fsa.grp4.clinic_appointment.dto.patient;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
}
