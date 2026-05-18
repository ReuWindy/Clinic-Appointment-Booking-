package fsa.grp4.clinic_appointment.service.implementation;

import fsa.grp4.clinic_appointment.dto.appointment.AppointmentRequest;
import fsa.grp4.clinic_appointment.dto.appointment.AppointmentResponse;
import fsa.grp4.clinic_appointment.dto.appointment.AppointmentStatusRequest;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.dto.doctor.DoctorScheduleRequest;
import fsa.grp4.clinic_appointment.dto.doctor.DoctorScheduleResponse;
import fsa.grp4.clinic_appointment.mapper.AdminDoctorMapper;
import fsa.grp4.clinic_appointment.entity.Appointment;
import fsa.grp4.clinic_appointment.entity.Doctor;
import fsa.grp4.clinic_appointment.entity.DoctorSchedule;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.exception.ConflictException;
import fsa.grp4.clinic_appointment.exception.NotFoundException;
import fsa.grp4.clinic_appointment.entity.AppointmentStatus;
import fsa.grp4.clinic_appointment.repository.contract.IAppointmentRepository;
import fsa.grp4.clinic_appointment.repository.contract.IDoctorRepository;
import fsa.grp4.clinic_appointment.repository.contract.IUserRepository;
import fsa.grp4.clinic_appointment.service.contract.IReceptionistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceptionistServiceImpl implements IReceptionistService {

    private final IDoctorRepository doctorRepository;
    private final IAppointmentRepository appointmentRepository;
    private final IUserRepository userRepository;
    private final AdminDoctorMapper adminDoctorMapper;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public DoctorScheduleResponse createSchedule(DoctorScheduleRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        if (doctorRepository.findScheduleByDoctorIdAndDate(request.getDoctorId(), request.getDate()).isPresent()) {
            throw new ConflictException("Schedule for this date already exists");
        }

        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        schedule.setIsActive(true);

        // Save the schedule directly to ensure ID is generated and returned
        doctor.getSchedules().add(schedule);
        doctorRepository.save(doctor);
        
        // Find the saved schedule in the list to get its ID
        DoctorSchedule savedSchedule = doctor.getSchedules().stream()
                .filter(s -> s.getDate().equals(schedule.getDate()) && s.getStartTime().equals(schedule.getStartTime()))
                .findFirst()
                .orElse(schedule);

        return mapToScheduleResponse(savedSchedule);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public DoctorScheduleResponse updateSchedule(int id, DoctorScheduleRequest request) {
        Doctor doctor = doctorRepository.findByScheduleId(id)
                .orElseThrow(() -> new NotFoundException("Schedule not found"));

        DoctorSchedule schedule = doctor.getSchedules().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Schedule not found"));

        schedule.setDate(request.getDate());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        doctorRepository.save(doctor);
        return mapToScheduleResponse(schedule);
    }

    @Override
    public List<DoctorScheduleResponse> getSchedulesByDoctorId(int doctorId) {
        return doctorRepository.findSchedulesByDoctorId(doctorId).stream()
                .map(this::mapToScheduleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorScheduleResponse> getAllSchedules() {
        return doctorRepository.findAllSchedules().stream()
                .map(this::mapToScheduleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminDoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(adminDoctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteSchedule(int id) {
        if (!doctorRepository.findScheduleById(id).isPresent()) {
            throw new NotFoundException("Schedule not found");
        }
        doctorRepository.deleteScheduleById(id);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        // 1. Check if slot is already taken
        if (appointmentRepository.existsActiveSlot(
                request.getDoctorId(), request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new ConflictException("Ca này đã có lịch, vui lòng chọn ca khác");
        }

        User patient;
        if (request.getPatientId() != null && request.getPatientId() > 0) {
            patient = userRepository.getById(request.getPatientId())
                    .orElseThrow(() -> new NotFoundException("Patient not found"));
        } else {
            // Create new walk-in patient
            String timestamp = String.valueOf(System.currentTimeMillis());
            patient = User.builder()
                    .fullName(request.getPatientName())
                    .username("walkin_" + timestamp)
                    .email("walkin_" + timestamp + "@clinic.com")
                    .role(fsa.grp4.clinic_appointment.entity.Role.PATIENT)
                    .build();
            patient = userRepository.add(patient);
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .reason(request.getReason())
                .status(fsa.grp4.clinic_appointment.entity.AppointmentStatus.PENDING)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToAppointmentResponse(savedAppointment);
    }

    @Override
    public AppointmentResponse updateAppointmentStatus(int appointmentId, AppointmentStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        appointment.setStatus(request.getStatus());
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return mapToAppointmentResponse(updatedAppointment);
    }

    @Override
    public List<AppointmentResponse> getAppointments(String date) {
        List<Appointment> appointments;
        try {
            if (date != null && !date.isEmpty()) {
                java.time.LocalDate localDate = java.time.LocalDate.parse(date);
                appointments = appointmentRepository.findByAppointmentDate(localDate);
            } else {
                appointments = appointmentRepository.findByStatus(AppointmentStatus.PENDING);
            }
        } catch (Exception e) {
            log.error("Error fetching appointments: {}", e.getMessage());
            appointments = appointmentRepository.findByStatus(AppointmentStatus.PENDING);
        }
        return appointments.stream()
                .map(this::mapToAppointmentResponse)
                .collect(Collectors.toList());
    }

    private DoctorScheduleResponse mapToScheduleResponse(DoctorSchedule schedule) {
        return DoctorScheduleResponse.builder()
                .id(schedule.getId())
                .doctorId(schedule.getDoctor().getId())
                .doctorName(schedule.getDoctor().getUser().getFullName())
                .date(schedule.getDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
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
