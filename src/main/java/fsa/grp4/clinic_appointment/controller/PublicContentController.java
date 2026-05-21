package fsa.grp4.clinic_appointment.controller;

import fsa.grp4.clinic_appointment.dto.publicapi.PublicDoctorResponse;
import fsa.grp4.clinic_appointment.dto.publicapi.PublicSpecialtyResponse;
import fsa.grp4.clinic_appointment.service.contract.IPublicContentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/public")
public class PublicContentController {
    private final IPublicContentService publicContentService;

    public PublicContentController(IPublicContentService publicContentService) {
        this.publicContentService = publicContentService;
    }

    @GetMapping("/specialties")
    @ResponseStatus(HttpStatus.OK)
    public List<PublicSpecialtyResponse> getSpecialties() {
        return publicContentService.getPublicSpecialties();
    }

    @GetMapping("/doctors")
    @ResponseStatus(HttpStatus.OK)
    public List<PublicDoctorResponse> getDoctors() {
        return publicContentService.getPublicDoctors();
    }

    @GetMapping("/doctors/specialty/{specialtyId}")
    @ResponseStatus(HttpStatus.OK)
    public List<PublicDoctorResponse> getDoctorsBySpecialty(@PathVariable int specialtyId) {
        return publicContentService.getPublicDoctorsBySpecialtyId(specialtyId);
    }
}
