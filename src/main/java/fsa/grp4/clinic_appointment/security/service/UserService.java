package fsa.grp4.clinic_appointment.security.service;

import fsa.grp4.clinic_appointment.dto.user.UserResponse;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.security.dto.*;

import java.util.List;

public interface UserService {
    User findByUsername(String username);

    List<UserResponse> listAllUser();

    RegistrationResponse registration(RegistrationRequest registrationRequest);

    AuthenticatedUserDto findAuthenticatedUserByUsername(String username);



}
