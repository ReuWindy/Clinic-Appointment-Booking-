package fsa.grp4.clinic_appointment.dto.publicapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicSpecialtyResponse {
    private int id;
    private String name;
    private String description;
}
