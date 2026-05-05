package fsa.grp4.clinic_appointment.dto.speciality;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecialityRequest {
    private String name;
    private String description;
}
