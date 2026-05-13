package fsa.grp4.clinic_appointment.dto.doctor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorScheduleRequest {
    @NotNull(message = "Doctor ID is required")
    @Min(value = 1, message = "Please select a valid doctor")
    Integer doctorId;

    @NotNull(message = "Date is required")
    LocalDate date;

    @NotNull(message = "Start time is required")
    LocalTime startTime;

    @NotNull(message = "End time is required")
    LocalTime endTime;
}
