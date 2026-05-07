package fsa.grp4.clinic_appointment.service.implementation;

import java.util.List;
import java.util.Optional;

import fsa.grp4.clinic_appointment.dao.contract.IUserDAO;
import fsa.grp4.clinic_appointment.dto.receptionist.ReceptionistRequest;
import fsa.grp4.clinic_appointment.dto.receptionist.ReceptionistResponse;
import fsa.grp4.clinic_appointment.entity.Role;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.mapper.IReceptionistMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final IUserDAO userDAO;
    private final IReceptionistMapper receptionistMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminServiceImpl(ISpecialtyDAO specialtyDAO, SpecialtyMapper specialtyMapper, IUserDAO userDAO, IReceptionistMapper receptionistMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.specialtyDAO = specialtyDAO;
        this.specialtyMapper = specialtyMapper;
        this.userDAO = userDAO;
        this.receptionistMapper = receptionistMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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

    @Override
    public ReceptionistResponse createReceptionist(ReceptionistRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Receptionist request cannot be null");
        }
        Optional<User> optionalUser = userDAO.findByUserName(request.getUsername());
        if (optionalUser.isPresent()) {
            throw new ConflictException("Receptionist username already exists");
        }

        User user = receptionistMapper.toEntity(request);
        user.setId(0);
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        User saveUser = userDAO.add(user);
        saveUser.setRole(Role.RECEPTIONIST);
        return receptionistMapper.toResponse(saveUser);
    }

    @Override
    public ReceptionistResponse updateReceptionist(int id, ReceptionistRequest request) {
        if (receptionistMapper == null) {
            throw new IllegalArgumentException("Receptionist request cannot be null");
        }

        Optional<User> optionalUser = userDAO.getById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Receptionist not found with id: " + id);
        }

        User existingUser = optionalUser.get();
        existingUser.setUsername(request.getUsername());
        existingUser.setPassword(request.getPassword());
        existingUser.setFullName(request.getFullName());
        existingUser.setPhone(request.getPhone());
        existingUser.setAddress(request.getAddress());
        existingUser.setEmail(request.getEmail());
        User updatedUser = userDAO.update(existingUser);
        return receptionistMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteReceptionist(String username) {
        Optional<User> optionalUser = userDAO.findByUserName(username);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Receptionist not found with id: " + username);
        }
        userDAO.deleteByUserName(username);
    }

    @Override
    public List<ReceptionistResponse> getAllReceptionists() {
        List<User> receptionists = userDAO.getAll().stream()
                .filter(user -> user.getRole() == Role.RECEPTIONIST)
                .toList();
        return receptionistMapper.toResponses(receptionists);
    }
}
