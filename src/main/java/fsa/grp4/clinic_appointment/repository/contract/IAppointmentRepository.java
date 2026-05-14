package fsa.grp4.clinic_appointment.repository.contract;

import fsa.grp4.clinic_appointment.entity.Appointment;
import fsa.grp4.clinic_appointment.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IAppointmentRepository extends JpaRepository<Appointment, Integer> {
    long countByDoctorIdAndAppointmentDate(int doctorId, LocalDate date);
    List<Appointment> findByDoctorIdAndAppointmentDateOrderByAppointmentTimeAsc(int doctorId, LocalDate date);
    long countByDoctorIdAndAppointmentDateAndStatus(int doctorId, LocalDate date, AppointmentStatus status);
    @Query("SELECT COUNT(DISTINCT a.patient.id) FROM Appointment a WHERE a.doctor.id = :doctorId")
    long countDistinctPatientsByDoctorId(@Param("doctorId") int doctorId);
    
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByAppointmentDate(LocalDate date);
    List<Appointment> findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(int patientId);
    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(int doctorId, LocalDate appointmentDate, LocalTime appointmentTime);

    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
            FROM Appointment a
            WHERE a.doctor.id = :doctorId
              AND a.appointmentDate = :appointmentDate
              AND a.appointmentTime = :appointmentTime
              AND a.status NOT IN (fsa.grp4.clinic_appointment.entity.AppointmentStatus.CANCELLED,
                                   fsa.grp4.clinic_appointment.entity.AppointmentStatus.REJECTED)
            """)
    boolean existsActiveSlot(
            @Param("doctorId") int doctorId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("appointmentTime") LocalTime appointmentTime
    );

    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
            FROM Appointment a
            WHERE a.doctor.id = :doctorId
              AND a.appointmentDate = :appointmentDate
              AND a.appointmentTime = :appointmentTime
              AND a.id <> :appointmentId
              AND a.status NOT IN (fsa.grp4.clinic_appointment.entity.AppointmentStatus.CANCELLED,
                                   fsa.grp4.clinic_appointment.entity.AppointmentStatus.REJECTED)
            """)
    boolean existsActiveSlotForAnotherAppointment(
            @Param("appointmentId") int appointmentId,
            @Param("doctorId") int doctorId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("appointmentTime") LocalTime appointmentTime
    );
}
