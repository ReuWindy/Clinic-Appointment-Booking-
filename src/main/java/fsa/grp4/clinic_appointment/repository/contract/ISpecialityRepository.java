package fsa.grp4.clinic_appointment.repository.contract;

import fsa.grp4.clinic_appointment.entity.Speciality;
import fsa.grp4.clinic_appointment.repository.IGenericRepository;

import java.util.Optional;

public interface ISpecialityRepository extends IGenericRepository<Speciality, Integer> {
    Optional<Speciality> getByName(String name);
}
