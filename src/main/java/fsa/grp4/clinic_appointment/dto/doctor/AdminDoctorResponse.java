package fsa.grp4.clinic_appointment.dto.doctor;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AdminDoctorResponse {
    int id;
    int userId;
    int specialtyId;

    //User
    String fullName;
    String email;
    String phone;
    boolean gender;

    //Doctor
    String specialtyName;
    BigDecimal fee;
    String experience;
    String avaUrl;
}
