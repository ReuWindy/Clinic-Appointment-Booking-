package fsa.grp4.clinic_appointment.mapper;

import fsa.grp4.clinic_appointment.dto.speciality.SpecialityRequest;
import fsa.grp4.clinic_appointment.dto.speciality.SpecialityResponse;
import fsa.grp4.clinic_appointment.entity.Speciality;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialityMapper {
    Speciality toEntity(SpecialityRequest request);

    SpecialityResponse toResponse(Speciality speciality);

    List<SpecialityResponse> toResponses(List<Speciality> specialities);
}
