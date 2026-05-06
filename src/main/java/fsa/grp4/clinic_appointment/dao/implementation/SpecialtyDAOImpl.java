package fsa.grp4.clinic_appointment.dao.implementation;

import fsa.grp4.clinic_appointment.dao.contract.ISpecialtyDAO;
import fsa.grp4.clinic_appointment.entity.Specialty;
import fsa.grp4.clinic_appointment.repository.contract.ISpecialtyRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpecialtyDAOImpl implements ISpecialtyDAO {

    private final ISpecialtyRepository specialtyRepository;

    @Override
    public Specialty add(Specialty specialty) {
        return specialtyRepository.add(specialty);
    }

    @Override
    public Specialty update(Specialty specialty) {
        return specialtyRepository.update(specialty);
    }

    @Override
    public void deleteById(int id) {
        specialtyRepository.getById(id).ifPresent(specialtyRepository::delete);
    }

    @Override
    public Optional<Specialty> findById(int id) {
        return specialtyRepository.getById(id);
    }

    @Override
    public List<Specialty> getAll() {
        return specialtyRepository.getAll();
    }

    @Override
    public boolean existsByName(String name) {
        return specialtyRepository.getByName(name).isPresent();
    }
}

