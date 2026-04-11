package fsa.grp4.clinic_appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ClinicAppointmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicAppointmentApplication.class, args);
	}

}
