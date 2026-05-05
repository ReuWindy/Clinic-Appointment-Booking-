package fsa.grp4.clinic_appointment.repository.implementation;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import fsa.grp4.clinic_appointment.entity.Specialty;
import fsa.grp4.clinic_appointment.repository.GenericRepositoryImpl;
import fsa.grp4.clinic_appointment.repository.contract.ISpecialtyRepository;
import jakarta.persistence.NoResultException;

@Repository
public class SpecialtyRepositoryImpl extends GenericRepositoryImpl<Specialty, Integer> implements ISpecialtyRepository {

    public SpecialtyRepositoryImpl() {
        super(Specialty.class);
    }

    @Override
    public Optional<Specialty> getByName(String name) {
        try {
            String jpql = "SELECT s FROM Specialty s WHERE s.name = :name";
            Specialty specialty = em.createQuery(jpql, Specialty.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(specialty);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
