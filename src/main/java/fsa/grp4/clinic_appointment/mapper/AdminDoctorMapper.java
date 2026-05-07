package fsa.grp4.clinic_appointment.mapper;

import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorRequest;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminDoctorMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.gender", target = "gender")
    @Mapping(source = "specialty.name", target = "specialtyName")
    AdminDoctorResponse toResponse(Doctor doctor);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "specialty", ignore = true)
    Doctor toEntity(AdminDoctorRequest request);
}