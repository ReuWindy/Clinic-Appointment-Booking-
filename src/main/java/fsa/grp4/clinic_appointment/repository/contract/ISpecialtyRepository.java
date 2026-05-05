package fsa.grp4.clinic_appointment.repository.contract;

import fsa.grp4.clinic_appointment.entity.Specialty;
import fsa.grp4.clinic_appointment.repository.IGenericRepository;

import java.util.Optional;

public interface ISpecialtyRepository extends IGenericRepository<Specialty, Integer> {
    Optional<Specialty> getByName(String name);
}
