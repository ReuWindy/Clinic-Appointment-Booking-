package fsa.grp4.clinic_appointment.dao.implementation;

import fsa.grp4.clinic_appointment.dao.contract.ISpecialityDAO;
import fsa.grp4.clinic_appointment.entity.Speciality;
import fsa.grp4.clinic_appointment.repository.contract.ISpecialityRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpecialityDAOImpl implements ISpecialityDAO {

    private final ISpecialityRepository specialityRepository;

    @Override
    public void add(Speciality speciality) {
        specialityRepository.add(speciality);
    }

    @Override
    public void update(Speciality speciality) {
        specialityRepository.update(speciality);
    }

    @Override
    public void deleteById(int id) {
        specialityRepository.getById(id).ifPresent(specialityRepository::delete);
    }

    @Override
    public Optional<Speciality> findById(int id) {
        return specialityRepository.getById(id);
    }

    @Override
    public List<Speciality> getAll() {
        return specialityRepository.getAll();
    }

    @Override
    public boolean existsByName(String name) {
        return specialityRepository.getByName(name).isPresent();
    }
}

