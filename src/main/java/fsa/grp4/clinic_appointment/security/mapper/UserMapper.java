package fsa.grp4.clinic_appointment.security.mapper;

import fsa.grp4.clinic_appointment.dto.user.UserResponse;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.security.dto.AuthenticatedUserDto;
import fsa.grp4.clinic_appointment.security.dto.RegistrationRequest;
import fsa.grp4.clinic_appointment.security.dto.UpdateUserRequest;
import fsa.grp4.clinic_appointment.security.dto.UpdateUserResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User convertToUser(RegistrationRequest registrationRequest);

    AuthenticatedUserDto convertToAuthenticatedUserDto(User user);

    UpdateUserResponse toUpdateResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget User user, UpdateUserRequest request);

    @Mapping(source = "phone", target = "phoneNumber")
    @Mapping(source = "isActive", target = "active")
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponses(List<User> users);
}