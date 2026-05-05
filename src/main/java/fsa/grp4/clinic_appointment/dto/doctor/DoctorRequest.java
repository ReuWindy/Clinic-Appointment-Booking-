package fsa.grp4.clinic_appointment.dto.doctor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorRequest {
    @NotNull(message = "User ID is required")
    int userId;

    @NotNull(message = "Speciality ID is required")
    int specialityId;

    @NotNull(message = "Experience is required")
    @Positive(message = "Experience must be positive")
    int experience;
}
