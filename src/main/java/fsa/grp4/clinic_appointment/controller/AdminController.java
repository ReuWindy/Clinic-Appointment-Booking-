package fsa.grp4.clinic_appointment.controller;

import fsa.grp4.clinic_appointment.dto.speciality.SpecialityRequest;
import fsa.grp4.clinic_appointment.dto.speciality.SpecialityResponse;
import fsa.grp4.clinic_appointment.service.contract.IAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminController {
    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/specialities")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSpeciality(@Valid @RequestBody SpecialityRequest request) {
        adminService.createSpeciality(request);
    }

    @PutMapping("/specialities/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateSpeciality(@PathVariable int id, @Valid @RequestBody SpecialityRequest request) {
        adminService.updateSpeciality(id, request);
    }

    @DeleteMapping("/specialities/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSpeciality(@PathVariable int id) {
        adminService.deleteSpeciality(id);
    }

    @GetMapping("/specialities")
    @ResponseStatus(HttpStatus.OK)
    public List<SpecialityResponse> getAllSpecialities() {
        return adminService.getAllSpecialities();
    }
}
