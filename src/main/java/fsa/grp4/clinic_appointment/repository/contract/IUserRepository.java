package fsa.grp4.clinic_appointment.repository.contract;

import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.repository.IGenericRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends IGenericRepository<User, Integer> {
    Optional<User> getByUsername(String username);

    Optional<User> getByEmail(String email);

    Optional<User> getByPhoneNumber(String phone);

    List<User> findAllUsers();
}
