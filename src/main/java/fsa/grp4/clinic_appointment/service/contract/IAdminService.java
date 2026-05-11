package fsa.grp4.clinic_appointment.service.contract;

import java.util.List;

import fsa.grp4.clinic_appointment.dto.patient.PatientRequest;
import fsa.grp4.clinic_appointment.dto.receptionist.ReceptionistRequest;
import fsa.grp4.clinic_appointment.dto.receptionist.ReceptionistResponse;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorRequest;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyRequest;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyResponse;
import fsa.grp4.clinic_appointment.dto.user.UserResponse;

public interface IAdminService {
    SpecialtyResponse createSpecialty(SpecialtyRequest specialtyRequest);

    SpecialtyResponse updateSpecialty(int id, SpecialtyRequest specialtyRequest);

    void deleteSpecialty(int id);

    List<SpecialtyResponse> getAllSpecialties();

    ReceptionistResponse createReceptionist(ReceptionistRequest request);

    ReceptionistResponse updateReceptionist(int id, ReceptionistRequest request);

    void deleteReceptionist(String username);

    List<ReceptionistResponse> getAllReceptionists();

    AdminDoctorResponse createDoctor(AdminDoctorRequest adminDoctorRequest);

    AdminDoctorResponse updateDoctor(int id, AdminDoctorRequest adminDoctorRequest);

    void deleteDoctor(int id);

    List<AdminDoctorResponse> getAllDoctors();

    public UserResponse updatePatient(int id, PatientRequest request);

    public void deletePatient(String username);

    List<UserResponse> getAllUsers();
}
