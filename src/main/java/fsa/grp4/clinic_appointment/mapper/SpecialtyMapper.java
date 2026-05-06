package fsa.grp4.clinic_appointment.mapper;

import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyRequest;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyResponse;
import fsa.grp4.clinic_appointment.entity.Specialty;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {
    Specialty toEntity(SpecialtyRequest request);

    SpecialtyResponse toResponse(Specialty specialty);

    List<SpecialtyResponse> toResponses(List<Specialty> specialities);
}
