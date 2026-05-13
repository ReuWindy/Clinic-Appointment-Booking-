package fsa.grp4.clinic_appointment.mapper;

import fsa.grp4.clinic_appointment.dto.receptionist.ReceptionistRequest;
import fsa.grp4.clinic_appointment.dto.receptionist.ReceptionistResponse;
import fsa.grp4.clinic_appointment.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IReceptionistMapper {
    User toEntity(ReceptionistRequest request);

    ReceptionistResponse toResponse(User user);

    List<ReceptionistResponse> toResponses(List<User> users);
}
