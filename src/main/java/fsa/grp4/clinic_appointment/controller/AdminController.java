package fsa.grp4.clinic_appointment.controller;

import java.util.List;

import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorRequest;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.dto.receptionist.ReceptionistResponse;
import fsa.grp4.clinic_appointment.dto.user.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyRequest;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyResponse;
import fsa.grp4.clinic_appointment.service.contract.IAdminService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/admin")
// @PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/specialties")
    @ResponseStatus(HttpStatus.CREATED)
    public SpecialtyResponse createSpecialty(@Valid @RequestBody SpecialtyRequest request) {
        return adminService.createSpecialty(request);
    }

    @PutMapping("/specialties/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SpecialtyResponse updateSpecialty(@PathVariable int id, @Valid @RequestBody SpecialtyRequest request) {
        return adminService.updateSpecialty(id, request);
    }

    @DeleteMapping("/specialties/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSpecialty(@PathVariable int id) {
        adminService.deleteSpecialty(id);
    }

    @GetMapping("/specialties")
    @ResponseStatus(HttpStatus.OK)
    public List<SpecialtyResponse> getAllSpecialties() {
        return adminService.getAllSpecialties();
    }

    @PostMapping("/doctors")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminDoctorResponse createDoctor(@Valid @RequestBody AdminDoctorRequest request) {
        return adminService.createDoctor(request);
    }

    @PutMapping("/doctors/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdminDoctorResponse updateSpecialty(@PathVariable int id, @Valid @RequestBody AdminDoctorRequest request) {
        return adminService.updateDoctor(id, request);
    }

    @DeleteMapping("/doctors/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDoctor(@PathVariable int id) {
        adminService.deleteDoctor(id);
    }

    @GetMapping("/doctors")
    @ResponseStatus(HttpStatus.OK)
    public List<AdminDoctorResponse> getAllDoctors() {
        return adminService.getAllDoctors();
    }


    @PostMapping("/receptionists")
    @ResponseStatus(HttpStatus.CREATED)
    public ReceptionistResponse createReceptionist(@Valid @RequestBody fsa.grp4.clinic_appointment.dto.receptionist.ReceptionistRequest request) {
        return adminService.createReceptionist(request);
    }

    @PutMapping("/receptionists/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReceptionistResponse updateReceptionist(@PathVariable int id, @Valid @RequestBody fsa.grp4.clinic_appointment.dto.receptionist.ReceptionistRequest request) {
        return adminService.updateReceptionist(id, request);
    }

    @DeleteMapping("receptionists/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReceptionist(@PathVariable String username) {
        adminService.deleteReceptionist(username);
    }

    @GetMapping("/receptionists")
    @ResponseStatus(HttpStatus.OK)
    public List<ReceptionistResponse> getAllReceptionists() {
        return adminService.getAllReceptionists();
    }

    @PutMapping("/patients/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateReceptionist(@PathVariable int id, @Valid @RequestBody fsa.grp4.clinic_appointment.dto.patient.PatientRequest request) {
        return adminService.updatePatient(id, request);
    }

    @DeleteMapping("patients/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePatient(@PathVariable String username) {
        adminService.deletePatient(username);
    }

    @GetMapping("/patients")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() {
        return adminService.getAllUsers();
    }
}
