package fsa.grp4.clinic_appointment.dao.contract;

import java.util.List;
import java.util.Optional;

import fsa.grp4.clinic_appointment.entity.Specialty;

public interface ISpecialtyDAO {
    Specialty add(Specialty specialty);

    Specialty update(Specialty specialty);

    void deleteById(int id);

    Optional<Specialty> findById(int id);

    List<Specialty> getAll();

    boolean existsByName(String name);
}
