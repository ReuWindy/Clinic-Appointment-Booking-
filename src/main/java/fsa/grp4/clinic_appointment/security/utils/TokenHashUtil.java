package fsa.grp4.clinic_appointment.security.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class TokenHashUtil {
    public static String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Cannot hash refresh token");
        }
    }
}
