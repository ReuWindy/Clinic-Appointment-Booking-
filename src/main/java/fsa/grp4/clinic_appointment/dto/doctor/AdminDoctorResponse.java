package fsa.grp4.clinic_appointment.dto.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AdminDoctorResponse {
    int id;
    int userId;

    //User
    String fullName;
    String email;
    String phone;
    boolean gender;

    //Doctor
    String specialtyName;
    BigDecimal fee;
    String experience;
}
