package fsa.grp4.clinic_appointment.security.service;

import fsa.grp4.clinic_appointment.repository.implementation.OtpTokenRepositoryImpl;
import fsa.grp4.clinic_appointment.repository.implementation.RefreshTokenRepositoryImpl;
import fsa.grp4.clinic_appointment.repository.implementation.UserRepositoryImpl;
import fsa.grp4.clinic_appointment.security.dto.VerifyOtpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fsa.grp4.clinic_appointment.entity.OtpToken;
import fsa.grp4.clinic_appointment.entity.User;
import fsa.grp4.clinic_appointment.security.dto.ResetPasswordRequest;
import java.time.LocalDateTime;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepositoryImpl userRepository;
    @Autowired
    private OtpTokenRepositoryImpl otpTokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RefreshTokenRepositoryImpl refreshTokenRepository;


    @Transactional
    public String generateAndSendOtp(String email) {
        User user = userRepository.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống"));

        String otp = String.format("%06d", new Random().nextInt(999999));

        otpTokenRepository.deleteByEmail(email);

        OtpToken otpToken = new OtpToken();
        otpToken.setEmail(email);
        otpToken.setOtp(otp);
        otpToken.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpTokenRepository.add(otpToken);

        emailService.sendOtpEmail(email, otp);

        return "Mã OTP đã được gửi đến email của bạn.";
    }
    @Transactional
    public String verifyOtp(VerifyOtpRequest request){
        OtpToken otpToken = otpTokenRepository.getByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu đổi mật khẩu cho email này"));

        if (!otpToken.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Mã OTP không chính xác");
        }

        if (otpToken.isExpired()) {
            otpTokenRepository.delete(otpToken);
            throw new RuntimeException("Mã OTP đã hết hạn. Vui lòng yêu cầu mã mới.");
        }
        otpToken.setVerified(true);
        return "Xác thực OTP thành công. Bạn có thể đặt lại mật khẩu ngay bây giờ!";
    }

    @Transactional
    public String resetPassword(ResetPasswordRequest request) {
        OtpToken otpToken = otpTokenRepository.getByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu đổi mật khẩu cho email này"));

        if (otpToken.isExpired()) {
            otpTokenRepository.delete(otpToken);
            throw new RuntimeException("Mã OTP đã hết hạn. Vui lòng yêu cầu mã mới.");
        }

        if(!otpToken.isVerified()){
            throw new RuntimeException("Mã OTP chưa được xác thực. Vui lòng xác thực mã OTP trước khi đặt lại mật khẩu.");
        }

        User user = userRepository.getByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.update(user);

//        refreshTokenRepository.deleteAllByUser(user);

        otpTokenRepository.delete(otpToken);

        return "Mật khẩu đã được đặt lại thành công!";
    }
    
}
