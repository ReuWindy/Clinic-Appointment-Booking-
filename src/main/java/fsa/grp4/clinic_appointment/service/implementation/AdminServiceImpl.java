package fsa.grp4.clinic_appointment.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fsa.grp4.clinic_appointment.dao.contract.ISpecialtyDAO;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyRequest;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyResponse;
import fsa.grp4.clinic_appointment.entity.Specialty;
import fsa.grp4.clinic_appointment.exception.ConflictException;
import fsa.grp4.clinic_appointment.exception.NotFoundException;
import fsa.grp4.clinic_appointment.mapper.SpecialtyMapper;
import fsa.grp4.clinic_appointment.service.contract.IAdminService;

@Service
@Transactional
public class AdminServiceImpl implements IAdminService {
    private final ISpecialtyDAO specialtyDAO;
    private final SpecialtyMapper specialtyMapper;

    public AdminServiceImpl(ISpecialtyDAO specialtyDAO, SpecialtyMapper specialtyMapper) {
        this.specialtyDAO = specialtyDAO;
        this.specialtyMapper = specialtyMapper;
    }

    @Override
    public SpecialtyResponse createSpecialty(SpecialtyRequest specialtyRequest) {
        if (specialtyRequest == null) {
            throw new IllegalArgumentException("Specialty request cannot be null");
        }
        if (specialtyDAO.existsByName(specialtyRequest.getName())) {
            throw new ConflictException("Specialty name already exists");
        }
        Specialty specialty = specialtyMapper.toEntity(specialtyRequest);
        specialty.setId(0);
        Specialty savedSpecialty = specialtyDAO.add(specialty);
        return specialtyMapper.toResponse(savedSpecialty);
    }

    @Override
    public SpecialtyResponse updateSpecialty(int id, SpecialtyRequest specialtyRequest) {
        if (specialtyRequest == null) {
            throw new IllegalArgumentException("Specialty request cannot be null");
        }

        Optional<Specialty> optionalSpecialty = specialtyDAO.findById(id);
        if (optionalSpecialty.isEmpty()) {
            throw new NotFoundException("Specialty not found with id: " + id);
        }

        Specialty existingSpecialty = optionalSpecialty.get();

        if (!existingSpecialty.getName().equalsIgnoreCase(specialtyRequest.getName()) &&
                specialtyDAO.existsByName(specialtyRequest.getName())) {
            throw new ConflictException("Specialty name already exists");
        }

        existingSpecialty.setName(specialtyRequest.getName());
        existingSpecialty.setDescription(specialtyRequest.getDescription());

        Specialty updatedSpecialty = specialtyDAO.update(existingSpecialty);
        return specialtyMapper.toResponse(updatedSpecialty);
    }

    @Override
    public void deleteSpecialty(int id) {
        Optional<Specialty> optionalSpecialty = specialtyDAO.findById(id);
        if (optionalSpecialty.isEmpty()) {
            throw new NotFoundException("Specialty not found with id: " + id);
        }
        specialtyDAO.deleteById(id);
    }

    @Override
    public List<SpecialtyResponse> getAllSpecialties() {
        return specialtyMapper.toResponses(specialtyDAO.getAll());
    }
}
