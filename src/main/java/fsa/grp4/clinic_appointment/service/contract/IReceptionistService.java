package fsa.grp4.clinic_appointment.service.contract;

import fsa.grp4.clinic_appointment.dto.appointment.AppointmentRequest;
import fsa.grp4.clinic_appointment.dto.appointment.AppointmentResponse;
import fsa.grp4.clinic_appointment.dto.appointment.AppointmentStatusRequest;
import fsa.grp4.clinic_appointment.dto.doctor.AdminDoctorResponse;
import fsa.grp4.clinic_appointment.dto.doctor.DoctorScheduleRequest;
import fsa.grp4.clinic_appointment.dto.doctor.DoctorScheduleResponse;

import java.util.List;

public interface IReceptionistService {
    DoctorScheduleResponse createSchedule(DoctorScheduleRequest request);
    DoctorScheduleResponse updateSchedule(int id, DoctorScheduleRequest request);
    List<DoctorScheduleResponse> getSchedulesByDoctorId(int doctorId);
    List<DoctorScheduleResponse> getAllSchedules();
    List<AdminDoctorResponse> getAllDoctors();
    void deleteSchedule(int id);
    
    AppointmentResponse createAppointment(AppointmentRequest request);
    AppointmentResponse updateAppointmentStatus(int appointmentId, AppointmentStatusRequest request);
    List<AppointmentResponse> getAppointments(String date);
}
