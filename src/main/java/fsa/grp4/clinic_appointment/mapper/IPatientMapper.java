package fsa.grp4.clinic_appointment.mapper;

import fsa.grp4.clinic_appointment.dto.patient.PatientRequest;
import fsa.grp4.clinic_appointment.dto.user.UserResponse;
import fsa.grp4.clinic_appointment.entity.User;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface IPatientMapper {
    List<UserResponse> toUserResponses(List<User> users);

    User toEntity(PatientRequest request);

    UserResponse toResponse(User user);
}
