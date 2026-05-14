package fsa.grp4.clinic_appointment.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        try {
            // Drop check constraint that might be out of date
            jdbcTemplate.execute("ALTER TABLE appointments DROP CONSTRAINT IF EXISTS appointments_status_check");
            // Sometimes Hibernate creates a constraint with a number
            jdbcTemplate.execute("ALTER TABLE appointments DROP CONSTRAINT IF EXISTS appointments_status_check1");
            jdbcTemplate.execute("ALTER TABLE appointments DROP CONSTRAINT IF EXISTS appointments_status_check2");
            System.out.println("Successfully dropped old appointments_status_check constraints.");
        } catch (Exception e) {
            System.err.println("Failed to drop check constraint: " + e.getMessage());
        }
    }
}
