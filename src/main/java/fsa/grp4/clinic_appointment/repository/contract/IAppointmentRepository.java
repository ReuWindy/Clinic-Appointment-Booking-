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
}
