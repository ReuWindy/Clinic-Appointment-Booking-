package fsa.grp4.clinic_appointment.controller;

import fsa.grp4.clinic_appointment.dto.appointment.DoctorAppointmentResponse;
import fsa.grp4.clinic_appointment.dto.doctor.DashboardStatsDTO;
import fsa.grp4.clinic_appointment.dto.patient.DoctorPatientListResponse;
import fsa.grp4.clinic_appointment.service.implementation.DoctorDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin("*")
public class DoctorController {

    @Autowired
    private DoctorDashboardService dashboardService;

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
}