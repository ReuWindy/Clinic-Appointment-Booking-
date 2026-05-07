package fsa.grp4.clinic_appointment.dto.receptionist;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReceptionistRequest {
    private String fullName;
    private String username;
    private String password;
    private String phone;
    private String address;
    private String email;
}
