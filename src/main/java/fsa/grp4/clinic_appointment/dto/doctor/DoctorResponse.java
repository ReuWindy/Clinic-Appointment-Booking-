package fsa.grp4.clinic_appointment.dto.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorResponse {
    private int id;
    private int userId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private String dob;
    private int specialityId;
    private String specialityName;
    private int experience;
}
