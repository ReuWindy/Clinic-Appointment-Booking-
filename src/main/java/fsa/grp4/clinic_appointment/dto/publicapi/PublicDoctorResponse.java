package fsa.grp4.clinic_appointment.dto.publicapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicDoctorResponse {
    private int id;
    private int specialtyId;
    private String fullName;
    private String specialtyName;
    private String experience;
    private String avaUrl;
}
