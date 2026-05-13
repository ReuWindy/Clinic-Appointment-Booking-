package fsa.grp4.clinic_appointment.controller;

import fsa.grp4.clinic_appointment.dto.appointment.AppointmentRequest;
import fsa.grp4.clinic_appointment.dto.appointment.AppointmentResponse;
import fsa.grp4.clinic_appointment.dto.appointment.AppointmentStatusRequest;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.dto.doctor.DoctorScheduleRequest;
import fsa.grp4.clinic_appointment.dto.doctor.DoctorScheduleResponse;
import fsa.grp4.clinic_appointment.service.contract.IReceptionistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receptionist")
@RequiredArgsConstructor
public class ReceptionistController {

    private final IReceptionistService receptionistService;

    // Schedule endpoints
    @PostMapping("/schedules")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorScheduleResponse createSchedule(@Valid @RequestBody DoctorScheduleRequest request) {
        return receptionistService.createSchedule(request);
    }

    @PutMapping("/schedules/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DoctorScheduleResponse updateSchedule(@PathVariable int id, @Valid @RequestBody DoctorScheduleRequest request) {
        return receptionistService.updateSchedule(id, request);
    }

    @GetMapping("/schedules/doctor/{doctorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorScheduleResponse> getSchedulesByDoctor(@PathVariable int doctorId) {
        return receptionistService.getSchedulesByDoctorId(doctorId);
    }

    @GetMapping("/schedules")
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorScheduleResponse> getAllSchedules() {
        return receptionistService.getAllSchedules();
    }

    @GetMapping("/doctors")
    @ResponseStatus(HttpStatus.OK)
    public List<AdminDoctorResponse> getAllDoctors() {
        return receptionistService.getAllDoctors();
    }

    @DeleteMapping("/schedules/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSchedule(@PathVariable int id) {
        receptionistService.deleteSchedule(id);
    }

    // Appointment endpoints
    @PostMapping("/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse createAppointment(@Valid @RequestBody AppointmentRequest request) {
        return receptionistService.createAppointment(request);
    }

    @PutMapping("/appointments/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse updateAppointmentStatus(@PathVariable int id, @Valid @RequestBody AppointmentStatusRequest request) {
        return receptionistService.updateAppointmentStatus(id, request);
    }

    @GetMapping("/appointments")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentResponse> getAppointments(@RequestParam(required = false) String date) {
        return receptionistService.getAppointments(date);
    }
}
