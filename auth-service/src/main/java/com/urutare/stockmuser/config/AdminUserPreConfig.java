package com.urutare.stockmuser.config;

import com.urutare.stockmuser.entity.Role;
import com.urutare.stockmuser.entity.User;
import com.urutare.stockmuser.models.ERole;
import com.urutare.stockmuser.repository.RoleRepository;
import com.urutare.stockmuser.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Component
@Slf4j
public class AdminUserPreConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AdminUserPreConfig(UserRepository userRepository, RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if roles are already pre-configured
        if (roleRepository.count() == 0) {
            // Create role entities
            Role adminRole = new Role(ERole.ADMIN);
            Role sellerRole = new Role(ERole.SELLER);
            Role buyerRole = new Role(ERole.BUYER);
            Role managerRole = new Role(ERole.MANAGER);
            Role userRole = new Role(ERole.USER);

            // Save roles to the repository
            roleRepository.saveAll(Arrays.asList(adminRole, sellerRole, buyerRole, managerRole, userRole));
            log.info("Roles pre-configured.");
        }
        // Check if admin user already exists
        if (!userRepository.existsByEmail("admin@urutare.com")) {
            // Retrieve the ADMIN role from the repository
            Role adminRole = roleRepository.findByName(ERole.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found."));
            // Create a new admin user
            User adminUser = new User();
            adminUser.setId(UUID.randomUUID());
            adminUser.setEmail("admin@urutare.com");
            adminUser.setPhoneNumber("250788888888");
            adminUser.setFirstName("Admin");
            adminUser.setLastName("Admin");
            adminUser.setPassword(passwordEncoder.encode("Admin123"));
            adminUser.getRoles().add(adminRole);
            adminUser.setEmailVerified(true);
            adminUser.setPhoneVerified(true);

            // Save the admin user to the repository
            userRepository.save(adminUser);
            log.info("Admin user pre-configured.");
        }
    }
}
