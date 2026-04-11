package fsa.grp4.clinic_appointment.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiSuccessResponse<T> {
    private String message;
    private int statusCode;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss")
    private LocalDateTime timestamp;

    private T data;
}
