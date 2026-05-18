package fsa.grp4.clinic_appointment.service.implementation;

import fsa.grp4.clinic_appointment.dto.appointment.DoctorAppointmentStatusUpdateRequest;
import fsa.grp4.clinic_appointment.dto.appointment.DoctorAppointmentResponse;
import fsa.grp4.clinic_appointment.dto.doctor.DashboardStatsDTO;
import fsa.grp4.clinic_appointment.dto.patient.DoctorPatientListResponse;
import fsa.grp4.clinic_appointment.entity.Appointment;
import fsa.grp4.clinic_appointment.entity.AppointmentStatus;
import fsa.grp4.clinic_appointment.exception.ConflictException;
import fsa.grp4.clinic_appointment.exception.NotFoundException;
import fsa.grp4.clinic_appointment.repository.contract.IAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<DoctorAppointmentResponse> getTodaySchedule(int doctorId) {
        LocalDate today = LocalDate.now();
        List<Appointment> list = appointmentRepository
                .findByDoctorIdAndAppointmentDateOrderByAppointmentTimeAsc(doctorId, today);

        return list.stream().map(app -> DoctorAppointmentResponse.builder()
                .id(app.getId())
                .patientName(app.getPatient().getFullName())
                .time(app.getAppointmentTime())
                .status(app.getStatus().name())
                .serviceType(app.getReason())
            .diagnosis(app.getDiagnosis())
                .build()).toList();
    }

    public List<DoctorPatientListResponse> getTodayPatients(int doctorId) {
        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndAppointmentDateOrderByAppointmentTimeAsc(doctorId, today);

        return appointments.stream().map(app -> {
            int age = 30; //fake

            return DoctorPatientListResponse.builder()
                    .time(app.getAppointmentTime())
                .patientName(app.getPatient().getFullName())
                .gender(app.getPatient().isGender() ? "Male" : "Female")
                .age(age)
                    .status(app.getStatus().name())
                .build();
        }).toList();
    }

    @Transactional
    public DoctorAppointmentResponse updateAppointmentStatus(
            int doctorId,
            int appointmentId,
            DoctorAppointmentStatusUpdateRequest request
    ) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        if (appointment.getDoctor().getId() != doctorId) {
            throw new NotFoundException("Appointment not found for this doctor");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ConflictException("Completed appointment cannot be modified");
        }

        if (request.getStatus() != AppointmentStatus.COMPLETED) {
            throw new IllegalArgumentException("Doctor can only mark appointment as COMPLETED");
        }

        String incomingDiagnosis = request.getDiagnosis();
        if (incomingDiagnosis != null) {
            incomingDiagnosis = incomingDiagnosis.trim();
            appointment.setDiagnosis(incomingDiagnosis.isEmpty() ? null : incomingDiagnosis);
        }

        String diagnosisForCompletion = appointment.getDiagnosis();
        if (diagnosisForCompletion == null || diagnosisForCompletion.trim().isEmpty()) {
            throw new IllegalArgumentException("Diagnosis is required before completing appointment");
        }

        appointment.setStatus(request.getStatus());
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return DoctorAppointmentResponse.builder()
                .id(savedAppointment.getId())
                .patientName(savedAppointment.getPatient().getFullName())
                .serviceType(savedAppointment.getReason())
                .diagnosis(savedAppointment.getDiagnosis())
                .time(savedAppointment.getAppointmentTime())
                .status(savedAppointment.getStatus().name())
                .build();
    }
}
