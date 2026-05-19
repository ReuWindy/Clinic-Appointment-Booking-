package fsa.grp4.clinic_appointment.security.service;

import fsa.grp4.clinic_appointment.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Mã OTP xác thực đặt lại mật khẩu");
        message.setText("Mã OTP của bạn là: " + otp + ". Mã này có hiệu lực trong 5 phút.");

        mailSender.send(message);
    }

    public void sendAppointmentConfirmedEmail(Appointment appointment) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appointment.getPatient().getEmail());
        message.setSubject("Appointment confirmed");
        message.setText(buildAppointmentConfirmedEmailBody(appointment));

        mailSender.send(message);
    }

    private String buildAppointmentConfirmedEmailBody(Appointment appointment) {
        return String.format("""
                Dear %s,

                Your appointment has been confirmed.

                Doctor: %s
                Specialty: %s
                Date: %s
                Time: %s
                Reason: %s

                Please arrive on time. Thank you for using our clinic appointment service.
                """,
                appointment.getPatient().getFullName(),
                appointment.getDoctor().getUser().getFullName(),
                appointment.getDoctor().getSpecialty().getName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getReason() == null || appointment.getReason().isBlank()
                        ? "N/A"
                        : appointment.getReason());
    }
}
