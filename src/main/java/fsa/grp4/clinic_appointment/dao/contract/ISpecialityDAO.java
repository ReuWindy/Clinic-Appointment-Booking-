package fsa.grp4.clinic_appointment.dao.contract;

import fsa.grp4.clinic_appointment.entity.Speciality;

import java.util.List;
import java.util.Optional;

public interface ISpecialityDAO {
    void add(Speciality speciality);

    void update(Speciality speciality);

    void deleteById(int id);

    Optional<Speciality> findById(int id);

    List<Speciality> getAll();

    boolean existsByName(String name);
}
