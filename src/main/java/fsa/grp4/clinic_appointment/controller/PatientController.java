package fsa.grp4.clinic_appointment.controller;

import fsa.grp4.clinic_appointment.dto.appointment.AppointmentRequest;
import fsa.grp4.clinic_appointment.dto.appointment.AppointmentResponse;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyResponse;
import fsa.grp4.clinic_appointment.service.contract.IPatientAppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientAppointmentService patientAppointmentService;

    @GetMapping("/appointments")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentResponse> getMyAppointments() {
        return patientAppointmentService.getMyAppointments();
    }

    @GetMapping("/appointments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse getMyAppointment(@PathVariable int id) {
        return patientAppointmentService.getMyAppointment(id);
    }

    @PostMapping("/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse createMyAppointment(@Valid @RequestBody AppointmentRequest request) {
        return patientAppointmentService.createMyAppointment(request);
    }

    @PutMapping("/appointments/{id}/reschedule")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse rescheduleMyAppointment(@PathVariable int id, @Valid @RequestBody AppointmentRequest request) {
        return patientAppointmentService.rescheduleMyAppointment(id, request);
    }

    @PutMapping("/appointments/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse cancelMyAppointment(@PathVariable int id) {
        return patientAppointmentService.cancelMyAppointment(id);
    }

    @GetMapping("/doctors")
    @ResponseStatus(HttpStatus.OK)
    public List<AdminDoctorResponse> getAvailableDoctors() {
        return patientAppointmentService.getAvailableDoctors();
    }

    @GetMapping("/specialties")
    @ResponseStatus(HttpStatus.OK)
    public List<SpecialtyResponse> getSpecialties() {
        return patientAppointmentService.getSpecialties();
    }
}
