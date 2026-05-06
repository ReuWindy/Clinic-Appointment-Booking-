package fsa.grp4.clinic_appointment.dto.doctorDashboard;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardStatsDTO {
    long todaysAppointments;
    int pendingReviews;
    long totalPatients;
    long completedToday;
}