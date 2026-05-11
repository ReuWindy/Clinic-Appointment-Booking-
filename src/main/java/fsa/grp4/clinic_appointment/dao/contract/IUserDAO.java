package fsa.grp4.clinic_appointment.dao.contract;

import fsa.grp4.clinic_appointment.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    User add(User user);
    User update(User user);
    void delete(User user);
    Optional<User> getById(Integer id);
    Optional<User> findByUserName(String username);
    Optional<User> getByEmail(String email);
    List<User> getAll();
    Optional<User> existsByPhoneNumber(String phoneNumber);
    Page<User> search(Specification<User> specification, Pageable pageable);
    Optional<User> deleteByUserName(String username);
}
