package fsa.grp4.clinic_appointment.dto.specialty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecialtyResponse {
    private int id;
    private String name;
    private String description;
}
