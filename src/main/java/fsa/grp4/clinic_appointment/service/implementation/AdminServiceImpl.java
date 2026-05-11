package fsa.grp4.clinic_appointment.service.implementation;

import java.util.List;
import java.util.Optional;

import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorRequest;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.dto.patient.PatientRequest;
import fsa.grp4.clinic_appointment.dto.user.UserResponse;
import fsa.grp4.clinic_appointment.entity.Doctor;
import fsa.grp4.clinic_appointment.entity.Role;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.mapper.AdminDoctorMapper;
import fsa.grp4.clinic_appointment.mapper.IPatientMapper;
import fsa.grp4.clinic_appointment.repository.contract.IDoctorRepository;
import fsa.grp4.clinic_appointment.repository.contract.IUserRepository;
import fsa.grp4.clinic_appointment.security.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import fsa.grp4.clinic_appointment.dao.contract.IUserDAO;
import fsa.grp4.clinic_appointment.dto.receptionist.ReceptionistRequest;
import fsa.grp4.clinic_appointment.dto.receptionist.ReceptionistResponse;
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
    private final IPatientMapper patientMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AdminDoctorMapper adminDoctorMapper;
    private final IUserRepository iUserRepository;

    private final IDoctorRepository iDoctorRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(ISpecialtyDAO specialtyDAO,
                            SpecialtyMapper specialtyMapper,
                            IUserDAO userDAO,
                            IReceptionistMapper receptionistMapper, IPatientMapper patientMapper,
                            BCryptPasswordEncoder bCryptPasswordEncoder,
                            AdminDoctorMapper adminDoctorMapper,
                            IUserRepository iUserRepository,
                            IDoctorRepository iDoctorRepository,
                            PasswordEncoder passwordEncoder) {
        this.specialtyDAO = specialtyDAO;
        this.specialtyMapper = specialtyMapper;
        this.userDAO = userDAO;
        this.receptionistMapper = receptionistMapper;
        this.patientMapper = patientMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.adminDoctorMapper = adminDoctorMapper;
        this.iUserRepository = iUserRepository;
        this.iDoctorRepository = iDoctorRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public AdminDoctorResponse createDoctor(AdminDoctorRequest request) {
        if (iUserRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }


        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .gender(request.isGender())
                .role(Role.DOCTOR)
                .build();
        user = iUserRepository.add(user);

        Specialty specialty = specialtyDAO.findById(request.getSpecialtyId())
                .orElseThrow(() -> new NotFoundException("Specialty not found"));

        Doctor doctor = Doctor.builder()
                .user(user)
                .specialty(specialty)
                .fee(request.getFee())
                .experience(request.getExperience())
                .build();

        Doctor savedDoctor = iDoctorRepository.save(doctor);
        return adminDoctorMapper.toResponse(savedDoctor);
    }

    @Override
    public AdminDoctorResponse updateDoctor(int id, AdminDoctorRequest request) {
        Doctor existingDoctor = iDoctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor not found with id: " + id));

        User user = existingDoctor.getUser();
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setGender(request.isGender());
        iUserRepository.add(user);

        if (existingDoctor.getSpecialty().getId() != request.getSpecialtyId()) {
            Specialty specialty = specialtyDAO.findById(request.getSpecialtyId())
                    .orElseThrow(() -> new NotFoundException("Specialty not found"));
            existingDoctor.setSpecialty(specialty);
        }
        existingDoctor.setFee(request.getFee());
        existingDoctor.setExperience(request.getExperience());

        return adminDoctorMapper.toResponse(iDoctorRepository.save(existingDoctor));
    }

    @Override
    public void deleteDoctor(int id) {
        Doctor doctor = iDoctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor not found"));
        iUserRepository.delete(doctor.getUser());
    }

    @Override
    public List<AdminDoctorResponse> getAllDoctors() {
        List<Doctor> doctors = iDoctorRepository.findAll();
        return doctors.stream()
                .map(adminDoctorMapper::toResponse)
                .toList();
    }
    @Override
    public UserResponse updatePatient(int id, PatientRequest request) {
        if (receptionistMapper == null) {
            throw new IllegalArgumentException("Patient request cannot be null");
        }

        Optional<User> optionalUser = userDAO.getById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Patient not found with id: " + id);
        }

        User existingUser = optionalUser.get();
        existingUser.setFullName(request.getFullName());
        existingUser.setPhone(request.getPhoneNumber());
        existingUser.setAddress(request.getAddress());
        existingUser.setEmail(request.getEmail());
        User updatedUser = userDAO.update(existingUser);
        return patientMapper.toResponse(updatedUser);
    }

    @Override
    public void deletePatient(String username) {
        Optional<User> optionalUser = userDAO.findByUserName(username);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Patient not found with id: " + username);
        }
        userDAO.deleteByUserName(username);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userDAO.getAll().stream()
                .filter(user -> user.getRole() == Role.PATIENT)
                .toList();
        return patientMapper.toUserResponses(users);
    }
}
