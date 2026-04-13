package fsa.grp4.clinic_appointment.security.dto;

import fsa.grp4.clinic_appointment.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String USERNAME_OR_PASSWORD_INVALID = "Invalid username or password.";
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final AuthenticatedUserDto authenticatedUser = userService.findAuthenticatedUserByUsername(username);

        if (authenticatedUser == null) {
            throw new UsernameNotFoundException(USERNAME_OR_PASSWORD_INVALID);
        }

        List<SimpleGrantedAuthority> authorities = authenticatedUser.getRoles()
                .stream()
                .map(roleName -> {
                    if (roleName.startsWith("ROLE_")) {
                        return new SimpleGrantedAuthority(roleName);
                    }
                    return new SimpleGrantedAuthority("ROLE_" + roleName);
                })
                .toList();

        return org.springframework.security.core.userdetails.User.builder()
                .username(authenticatedUser.getUsername())
                .password(authenticatedUser.getPassword())
                .authorities(authorities)
                .build();
    }
}
