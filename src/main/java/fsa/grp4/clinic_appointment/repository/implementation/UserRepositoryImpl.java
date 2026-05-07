package fsa.grp4.clinic_appointment.repository.implementation;

import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.repository.GenericRepositoryImpl;
import fsa.grp4.clinic_appointment.repository.contract.IUserRepository;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<User, Integer> implements IUserRepository {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        try {
            String jpql = "SELECT u FROM User u WHERE u.username = :username";
            User user = em.createQuery(jpql, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getByEmail(String email) {
        try {
            String jpql = "SELECT u FROM User u WHERE u.email = :email";
            User user = em.createQuery(jpql, User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getByPhoneNumber(String phone) {
        try {
            String jpql = "SELECT u FROM User u WHERE u.phone = :phone";
            User user = em.createQuery(jpql, User.class)
                    .setParameter("phone", phone)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAllUsers() {
        String jpql = "SELECT u FROM User u";
        return em.createQuery(jpql, User.class).getResultList();
    }

    @Override
    public boolean existsByEmail(String email) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }
}

