package fsa.grp4.clinic_appointment.service.implementation;

import fsa.grp4.clinic_appointment.dto.doctor.AppointmentDTO;
import fsa.grp4.clinic_appointment.dto.doctor.DashboardStatsDTO;
import fsa.grp4.clinic_appointment.dto.doctor.PatientListDTO;
import fsa.grp4.clinic_appointment.entity.Appointment;
import fsa.grp4.clinic_appointment.entity.AppointmentStatus;
import fsa.grp4.clinic_appointment.repository.contract.IAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DoctorDashboardService {
    @Autowired
    private IAppointmentRepository appointmentRepository;

    public DashboardStatsDTO getDoctorStats(int doctorId) {
        LocalDate today = LocalDate.now();

        long totalToday = appointmentRepository.countByDoctorIdAndAppointmentDate(doctorId, today);

        long completedToday = appointmentRepository.countByDoctorIdAndAppointmentDateAndStatus(
                doctorId, today, AppointmentStatus.COMPLETED);

        long totalPatients = appointmentRepository.countDistinctPatientsByDoctorId(doctorId);

        return DashboardStatsDTO.builder()
                .todaysAppointments(totalToday)
                .completedToday(completedToday)
                .totalPatients(totalPatients)
                .pendingReviews(3)
                .build();
    }

    public List<AppointmentDTO> getTodaySchedule(int doctorId) {
        LocalDate today = LocalDate.now();
        List<Appointment> list = appointmentRepository
                .findByDoctorIdAndAppointmentDateOrderByAppointmentTimeAsc(doctorId, today);

        return list.stream().map(app -> AppointmentDTO.builder()
                .id(app.getId())
                .patientName(app.getPatient().getFullName())
                .time(app.getAppointmentTime())
                .status(app.getStatus().name())
                .serviceType(app.getReason())
                .build()).toList();
    }

    public List<PatientListDTO> getTodayPatients(int doctorId) {
        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndAppointmentDateOrderByAppointmentTimeAsc(doctorId, today);

        return appointments.stream().map(app -> {
            int age = 30; //fake

            return PatientListDTO.builder()
                    .time(app.getAppointmentTime())
                .patientName(app.getPatient().getFullName())
                .gender(app.getPatient().isGender() ? "Male" : "Female")
                .age(age)
                    .status(app.getStatus().name())
                .build();
        }).toList();
    }
}
