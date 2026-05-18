package fsa.grp4.clinic_appointment.service.implementation;

import fsa.grp4.clinic_appointment.dto.appointment.AppointmentRequest;
import fsa.grp4.clinic_appointment.dto.appointment.AppointmentResponse;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyResponse;
import fsa.grp4.clinic_appointment.entity.Appointment;
import fsa.grp4.clinic_appointment.entity.AppointmentStatus;
import fsa.grp4.clinic_appointment.entity.Doctor;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.exception.ConflictException;
import fsa.grp4.clinic_appointment.exception.NotFoundException;
import fsa.grp4.clinic_appointment.mapper.AdminDoctorMapper;
import fsa.grp4.clinic_appointment.mapper.SpecialtyMapper;
import fsa.grp4.clinic_appointment.repository.contract.IAppointmentRepository;
import fsa.grp4.clinic_appointment.repository.contract.IDoctorRepository;
import fsa.grp4.clinic_appointment.repository.contract.ISpecialtyRepository;
import fsa.grp4.clinic_appointment.repository.contract.IUserRepository;
import fsa.grp4.clinic_appointment.security.utils.SecurityConstants;
import fsa.grp4.clinic_appointment.service.contract.IPatientAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientAppointmentServiceImpl implements IPatientAppointmentService {

    private final IAppointmentRepository appointmentRepository;
    private final IDoctorRepository doctorRepository;
    private final IUserRepository userRepository;
    private final ISpecialtyRepository specialtyRepository;
    private final AdminDoctorMapper adminDoctorMapper;
    private final SpecialtyMapper specialtyMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getMyAppointments() {
        User patient = getAuthenticatedPatient();
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(patient.getId())
                .stream()
                .map(this::mapToAppointmentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getMyAppointment(int appointmentId) {
        return mapToAppointmentResponse(getOwnedAppointment(appointmentId));
    }

    @Override
    @Transactional
    public AppointmentResponse createMyAppointment(AppointmentRequest request) {
        if (appointmentRepository.existsActiveSlot(
                request.getDoctorId(), request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new ConflictException("This time slot is already booked. Please choose another time.");
        }

        User patient = getAuthenticatedPatient();
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .reason(request.getReason())
                .status(AppointmentStatus.PENDING)
                .build();

        return mapToAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public AppointmentResponse rescheduleMyAppointment(int appointmentId, AppointmentRequest request) {
        Appointment appointment = getOwnedAppointment(appointmentId);
        if (appointment.getStatus() == AppointmentStatus.CANCELLED || appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ConflictException("This appointment can no longer be rescheduled.");
        }
        if (appointmentRepository.existsActiveSlotForAnotherAppointment(
                appointmentId, request.getDoctorId(), request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new ConflictException("This time slot is already booked. Please choose another time.");
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.PENDING);

        return mapToAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public AppointmentResponse cancelMyAppointment(int appointmentId) {
        Appointment appointment = getOwnedAppointment(appointmentId);
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ConflictException("Completed appointments cannot be cancelled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return mapToAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminDoctorResponse> getAvailableDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(adminDoctorMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialtyResponse> getSpecialties() {
        return specialtyMapper.toResponses(specialtyRepository.getAll());
    }

    private Appointment getOwnedAppointment(int appointmentId) {
        User patient = getAuthenticatedPatient();
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        if (appointment.getPatient().getId() != patient.getId()) {
            throw new NotFoundException("Appointment not found");
        }
        return appointment;
    }

    private User getAuthenticatedPatient() {
        String username = SecurityConstants.getAuthenticatedUsername();
        return userRepository.getByUsername(username)
                .orElseThrow(() -> new NotFoundException("Patient not found"));
    }

    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .doctorId(appointment.getDoctor().getId())
                .patientName(appointment.getPatient().getFullName())
                .doctorName(appointment.getDoctor().getUser().getFullName())
                .specialtyName(appointment.getDoctor().getSpecialty().getName())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .diagnosis(appointment.getDiagnosis())
                .status(appointment.getStatus())
                .build();
    }
}
