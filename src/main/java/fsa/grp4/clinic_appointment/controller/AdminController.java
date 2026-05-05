package fsa.grp4.clinic_appointment.controller;

import java.util.List;

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
public class AdminController {
    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/specialities")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSpecialty(@Valid @RequestBody SpecialtyRequest request) {
        adminService.createSpecialty(request);
    }

    @PutMapping("/specialities/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateSpecialty(@PathVariable int id, @Valid @RequestBody SpecialtyRequest request) {
        adminService.updateSpecialty(id, request);
    }

    @DeleteMapping("/specialities/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSpecialty(@PathVariable int id) {
        adminService.deleteSpecialty(id);
    }

    @GetMapping("/specialities")
    @ResponseStatus(HttpStatus.OK)
    public List<SpecialtyResponse> getAllSpecialties() {
        return adminService.getAllSpecialties();
    }
}
