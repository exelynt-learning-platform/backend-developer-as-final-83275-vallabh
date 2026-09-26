package com.exelynt.resourcebooking.config;

import com.exelynt.resourcebooking.entity.User;
import com.exelynt.resourcebooking.enums.Role;
import com.exelynt.resourcebooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    public CommandLineRunner seedAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${admin.email}") String adminEmail,
            @Value("${admin.password}") String adminPassword
    ) {
        return args -> {

            if (userRepository.existsByEmail(adminEmail)) {
                return;
            }

            User admin = new User(
                    adminEmail,
                    passwordEncoder.encode(adminPassword),
                    Role.ADMIN
            );

            userRepository.save(admin);
        };
    }
}