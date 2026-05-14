package fsa.grp4.clinic_appointment.service.contract;

import fsa.grp4.clinic_appointment.dto.appointment.AppointmentRequest;
import fsa.grp4.clinic_appointment.dto.appointment.AppointmentResponse;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.dto.specialty.SpecialtyResponse;

import java.util.List;

public interface IPatientAppointmentService {
    List<AppointmentResponse> getMyAppointments();
    AppointmentResponse getMyAppointment(int appointmentId);
    AppointmentResponse createMyAppointment(AppointmentRequest request);
    AppointmentResponse rescheduleMyAppointment(int appointmentId, AppointmentRequest request);
    AppointmentResponse cancelMyAppointment(int appointmentId);
    List<AdminDoctorResponse> getAvailableDoctors();
    List<SpecialtyResponse> getSpecialties();
}
