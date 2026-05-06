package fsa.grp4.clinic_appointment.repository.contract;

import fsa.grp4.clinic_appointment.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IDoctorRepository extends JpaRepository<Doctor, Integer> {
}
