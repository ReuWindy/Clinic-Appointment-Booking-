package fsa.grp4.clinic_appointment.repository.contract;

import fsa.grp4.clinic_appointment.entity.Doctor;
import fsa.grp4.clinic_appointment.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IDoctorRepository extends JpaRepository<Doctor, Integer> {
    @Query("SELECT s FROM DoctorSchedule s WHERE s.doctor.id = :doctorId")
    List<DoctorSchedule> findSchedulesByDoctorId(@Param("doctorId") int doctorId);

    @Query("SELECT s FROM DoctorSchedule s")
    List<DoctorSchedule> findAllSchedules();

    @Query("SELECT s FROM DoctorSchedule s WHERE s.doctor.id = :doctorId AND s.date = :date")
    Optional<DoctorSchedule> findScheduleByDoctorIdAndDate(@Param("doctorId") int doctorId, @Param("date") LocalDate date);

    @Query("SELECT s FROM DoctorSchedule s WHERE s.id = :id")
    Optional<DoctorSchedule> findScheduleById(@Param("id") int id);

    @Modifying
    @Query("DELETE FROM DoctorSchedule s WHERE s.id = :id")
    void deleteScheduleById(@Param("id") int id);

    @Query("SELECT d FROM Doctor d JOIN d.schedules s WHERE s.id = :scheduleId")
    Optional<Doctor> findByScheduleId(@Param("scheduleId") int scheduleId);

    @Query("SELECT d FROM Doctor d WHERE d.user.username = :username")
    Optional<Doctor> findByUserUsername(@Param("username") String username);
}
