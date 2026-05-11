package fsa.grp4.clinic_appointment.security.service;

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
}
