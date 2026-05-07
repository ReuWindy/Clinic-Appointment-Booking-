package fsa.grp4.clinic_appointment.dto.doctor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminDoctorRequest {
    //User
    String fullName;
    String email;
    String phone;
    String address;
    boolean gender;
    String password;

    //Doctor
    int specialtyId;
    BigDecimal fee;
    String experience;
}
