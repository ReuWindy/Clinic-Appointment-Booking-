package fsa.grp4.clinic_appointment.service.contract;

import java.util.List;

import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyRequest;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyResponse;

public interface IAdminService {
    SpecialtyResponse createSpecialty(SpecialtyRequest specialtyRequest);

    SpecialtyResponse updateSpecialty(int id, SpecialtyRequest specialtyRequest);

    void deleteSpecialty(int id);

    List<SpecialtyResponse> getAllSpecialties();
}
