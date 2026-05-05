package fsa.grp4.clinic_appointment.repository.implementation;

import fsa.grp4.clinic_appointment.entity.Speciality;
import fsa.grp4.clinic_appointment.repository.GenericRepositoryImpl;
import fsa.grp4.clinic_appointment.repository.contract.ISpecialityRepository;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpecialityRepositoryImpl extends GenericRepositoryImpl<Speciality, Integer> implements ISpecialityRepository {

    public SpecialityRepositoryImpl() {
        super(Speciality.class);
    }

    @Override
    public Optional<Speciality> getByName(String name) {
        try {
            String jpql = "SELECT s FROM Speciality s WHERE s.name = :name";
            Speciality speciality = em.createQuery(jpql, Speciality.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(speciality);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
