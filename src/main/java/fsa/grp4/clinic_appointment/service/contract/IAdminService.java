package fsa.grp4.clinic_appointment.service.contract;

import java.util.List;

import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorRequest;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyRequest;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyResponse;

public interface IAdminService {
    SpecialtyResponse createSpecialty(SpecialtyRequest specialtyRequest);

    SpecialtyResponse updateSpecialty(int id, SpecialtyRequest specialtyRequest);

    void deleteSpecialty(int id);

    List<SpecialtyResponse> getAllSpecialties();

    AdminDoctorResponse createDoctor(AdminDoctorRequest adminDoctorRequest);

    AdminDoctorResponse updateDoctor(int id, AdminDoctorRequest adminDoctorRequest);

    void deleteDoctor(int id);

    List<AdminDoctorResponse> getAllDoctors();

}
