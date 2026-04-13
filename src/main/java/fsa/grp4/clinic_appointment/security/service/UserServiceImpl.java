package fsa.grp4.clinic_appointment.security.service;

import fsa.grp4.clinic_appointment.dto.user.UserResponse;
import fsa.grp4.clinic_appointment.entity.Role;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.exception.ConflictException;
import fsa.grp4.clinic_appointment.security.dto.AuthenticatedUserDto;
import fsa.grp4.clinic_appointment.security.dto.RegistrationRequest;
import fsa.grp4.clinic_appointment.security.dto.RegistrationResponse;
import fsa.grp4.clinic_appointment.security.jwt.JwtTokenManager;
import fsa.grp4.clinic_appointment.security.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{
    private final JwtTokenManager jwtTokenManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final UserDAO userDAO;

    public UserServiceImpl(JwtTokenManager jwtTokenManager, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper, UserDAO userDAO) {
        this.jwtTokenManager = jwtTokenManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
        this.userDAO = userDAO;
    }

    @Override
    public User findByUsername(String username) {
        if (username == null) {
            return null;
        }
        Optional<User> user = userDAO.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    public List<UserResponse> listAllUser() {
        return userMapper.(userDAO.findAll());
    }


    @Override
    public RegistrationResponse registration(RegistrationRequest registrationRequest) {
        if (userDAO.existsByPhoneNumber(registrationRequest.getPhone())) {
            throw new ConflictException("Phone number already exists");
        }

        if (registrationRequest.getEmail() != null && userDAO.existsByEmail(registrationRequest.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        User user = User.builder()
                .username(registrationRequest.getUsername())
                .password(bCryptPasswordEncoder.encode(registrationRequest.getPassword()))
                .phone(registrationRequest.getPhone())
                .email(registrationRequest.getEmail())
                .gender(registrationRequest.isGender())
                .build();

        user.setIsActive(true);

        userDAO.save(user);

        final String username = user.getUsername();
        final String registrationSuccessMessage = "Registration successful! " + username;
        return new RegistrationResponse(registrationSuccessMessage);
    }

    @Override
    public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {
        final User user = findByUsername(username);
        return userMapper.convertToAuthenticatedUserDto(user);
    }


}
