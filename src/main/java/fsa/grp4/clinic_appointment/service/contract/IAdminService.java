package fsa.grp4.clinic_appointment.service.contract;

import fsa.grp4.clinic_appointment.dto.speciality.SpecialityRequest;
import fsa.grp4.clinic_appointment.dto.speciality.SpecialityResponse;

import java.util.List;

public interface IAdminService {
    void createSpeciality(SpecialityRequest specialityRequest);

    void updateSpeciality(int id, SpecialityRequest specialityRequest);

    void deleteSpeciality(int id);

    List<SpecialityResponse> getAllSpecialities();
}
