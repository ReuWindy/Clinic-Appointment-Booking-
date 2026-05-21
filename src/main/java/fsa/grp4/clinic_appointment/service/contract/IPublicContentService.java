package fsa.grp4.clinic_appointment.service.contract;

import java.util.List;

import fsa.grp4.clinic_appointment.dto.publicapi.PublicDoctorResponse;
import fsa.grp4.clinic_appointment.dto.publicapi.PublicSpecialtyResponse;

public interface IPublicContentService {
    List<PublicSpecialtyResponse> getPublicSpecialties();

    List<PublicDoctorResponse> getPublicDoctors();

    List<PublicDoctorResponse> getPublicDoctorsBySpecialtyId(int specialtyId);
}
