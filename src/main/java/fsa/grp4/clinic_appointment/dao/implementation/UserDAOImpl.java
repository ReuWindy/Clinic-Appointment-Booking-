package fsa.grp4.clinic_appointment.dao.implementation;


import fsa.grp4.clinic_appointment.dao.contract.IUserDAO;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.repository.contract.IUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDAOImpl implements IUserDAO {

    private final IUserRepository userRepository;

    public UserDAOImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User add(User user) {
        return userRepository.add(user);
    }

    @Override
    public User update(User user) {
        return userRepository.update(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<User> getById(Integer id) {
        return userRepository.getById(id);
    }

    @Override
    public Optional<User> findByUserName(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public Optional<User> existsByPhoneNumber(String phoneNumber) {
        return userRepository.getByPhoneNumber(phoneNumber);
    }

    @Override
    public Page<User> search(Specification<User> specification, Pageable pageable) {
        return userRepository.search(specification, pageable);
    }
}
