package fsa.grp4.clinic_appointment.service.implementation;

import fsa.grp4.clinic_appointment.dao.contract.ISpecialityDAO;
import fsa.grp4.clinic_appointment.dto.speciality.SpecialityRequest;
import fsa.grp4.clinic_appointment.dto.speciality.SpecialityResponse;
import fsa.grp4.clinic_appointment.entity.Speciality;
import fsa.grp4.clinic_appointment.exception.ConflictException;
import fsa.grp4.clinic_appointment.exception.NotFoundException;
import fsa.grp4.clinic_appointment.mapper.SpecialityMapper;
import fsa.grp4.clinic_appointment.service.contract.IAdminService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminServiceImpl implements IAdminService {
    private final ISpecialityDAO specialityDAO;
    private final SpecialityMapper specialityMapper;

    public AdminServiceImpl(ISpecialityDAO specialityDAO, SpecialityMapper specialityMapper) {
        this.specialityDAO = specialityDAO;
        this.specialityMapper = specialityMapper;
    }

    @Override
    public void createSpeciality(SpecialityRequest specialityRequest) {
        if (specialityRequest == null) {
            throw new IllegalArgumentException("Speciality request cannot be null");
        }
        if (specialityDAO.existsByName(specialityRequest.getName())) {
            throw new ConflictException("Speciality name already exists");
        }
        Speciality speciality = specialityMapper.toEntity(specialityRequest);
        speciality.setId(0);
        specialityDAO.add(speciality);
    }

    @Override
    public void updateSpeciality(int id, SpecialityRequest specialityRequest) {
        if (specialityRequest == null) {
            throw new IllegalArgumentException("Speciality request cannot be null");
        }

        Optional<Speciality> optionalSpeciality = specialityDAO.findById(id);
        if (optionalSpeciality.isEmpty()) {
            throw new NotFoundException("Speciality not found with id: " + id);
        }

        Speciality existingSpeciality = optionalSpeciality.get();

        if (!existingSpeciality.getName().equalsIgnoreCase(specialityRequest.getName()) &&
                specialityDAO.existsByName(specialityRequest.getName())) {
            throw new ConflictException("Speciality name already exists");
        }

        existingSpeciality.setName(specialityRequest.getName());
        existingSpeciality.setDescription(specialityRequest.getDescription());

        specialityDAO.update(existingSpeciality);
    }

    @Override
    public void deleteSpeciality(int id) {
        Optional<Speciality> optionalSpeciality = specialityDAO.findById(id);
        if (optionalSpeciality.isEmpty()) {
            throw new NotFoundException("Speciality not found with id: " + id);
        }
        specialityDAO.deleteById(id);
    }

    @Override
    public List<SpecialityResponse> getAllSpecialities() {
        return specialityMapper.toResponses(specialityDAO.getAll());
    }
}
