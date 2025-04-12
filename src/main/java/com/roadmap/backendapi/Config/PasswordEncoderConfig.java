package com.roadmap.backendapi.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for PasswordEncoder.
 * This class is responsible for creating a PasswordEncoder bean using BCryptPasswordEncoder.
 * The PasswordEncoder bean is used for encoding passwords securely.
 *
 * @see PasswordEncoder
 * @see BCryptPasswordEncoder
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Creates a PasswordEncoder bean using BCryptPasswordEncoder.
     *
     * @return a PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
