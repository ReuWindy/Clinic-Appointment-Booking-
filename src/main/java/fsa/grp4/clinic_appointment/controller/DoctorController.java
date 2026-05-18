package fsa.grp4.clinic_appointment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fsa.grp4.clinic_appointment.dto.appointment.DoctorAppointmentResponse;
import fsa.grp4.clinic_appointment.dto.appointment.DoctorAppointmentStatusUpdateRequest;
import fsa.grp4.clinic_appointment.dto.doctor.DashboardStatsDTO;
import fsa.grp4.clinic_appointment.dto.doctor.DoctorMeResponse;
import fsa.grp4.clinic_appointment.dto.patient.DoctorPatientListResponse;
import fsa.grp4.clinic_appointment.entity.Doctor;
import fsa.grp4.clinic_appointment.exception.NotFoundException;
import fsa.grp4.clinic_appointment.repository.contract.IDoctorRepository;
import fsa.grp4.clinic_appointment.service.implementation.DoctorDashboardService;
import fsa.grp4.clinic_appointment.security.utils.SecurityConstants;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin("*")
public class DoctorController {

    @Autowired
    private DoctorDashboardService dashboardService;

    @Autowired
    private IDoctorRepository doctorRepository;

    @GetMapping("/me")
    public ResponseEntity<DoctorMeResponse> getCurrentDoctor() {
        String username = SecurityConstants.getAuthenticatedUsername();
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() -> new NotFoundException("Doctor profile not found"));

        return ResponseEntity.ok(new DoctorMeResponse(
                doctor.getId(),
                doctor.getUser().getId(),
                doctor.getUser().getFullName()
        ));
    }

    @GetMapping("/dashboard/{doctorId}")
    public ResponseEntity<DashboardStatsDTO> getStats(@PathVariable int doctorId) {
        return ResponseEntity.ok(dashboardService.getDoctorStats(doctorId));
    }

    @GetMapping("/schedule/{doctorId}")
    public ResponseEntity<List<DoctorAppointmentResponse>> getSchedule(@PathVariable int doctorId) {
        return ResponseEntity.ok(dashboardService.getTodaySchedule(doctorId));
    }

    @GetMapping("/patients/{doctorId}")
    public ResponseEntity<List<DoctorPatientListResponse>> getTodayPatients(@PathVariable int doctorId) {
        return ResponseEntity.ok(dashboardService.getTodayPatients(doctorId));
    }

    @PutMapping("/appointments/{doctorId}/{appointmentId}/status")
    public ResponseEntity<DoctorAppointmentResponse> updateAppointmentStatus(
            @PathVariable int doctorId,
            @PathVariable int appointmentId,
            @Valid @RequestBody DoctorAppointmentStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(dashboardService.updateAppointmentStatus(doctorId, appointmentId, request));
    }

}