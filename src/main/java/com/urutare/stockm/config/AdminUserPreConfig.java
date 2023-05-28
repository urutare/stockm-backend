package com.urutare.stockm.config;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.urutare.stockm.entity.Role;
import com.urutare.stockm.entity.User;
import com.urutare.stockm.models.ERole;
import com.urutare.stockm.repository.RoleRepository;
import com.urutare.stockm.repository.UserRepository;

@Component
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
            System.out.println("Roles pre-configured.");
        }
        // Check if admin user already exists
        if (!userRepository.existsByUsername("admin")) {
            // Retrieve the ADMIN role from the repository
            Role adminRole = roleRepository.findByName(ERole.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found."));
            // Create a new admin user
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@urutare.com");
            adminUser.setFullName("Admin");
            adminUser.setPassword(passwordEncoder.encode("Admin123"));
            adminUser.getRoles().add(adminRole);
            adminUser.setVerified(true);

            // Save the admin user to the repository
            userRepository.save(adminUser);
            System.out.println("Admin user pre-configured.");
        }
    }
}
