package com.urutare.stockm.service;

import com.urutare.stockm.constants.Properties;
import com.urutare.stockm.dto.request.AddRoleBody;
import com.urutare.stockm.dto.request.AssignRoleBody;
import com.urutare.stockm.entity.BlockedToken;
import com.urutare.stockm.entity.ResetPasswordToken;
import com.urutare.stockm.entity.Role;
import com.urutare.stockm.entity.User;
import com.urutare.stockm.exception.ConflictException;
import com.urutare.stockm.exception.ForbiddenException;
import com.urutare.stockm.exception.ResourceNotFoundException;
import com.urutare.stockm.models.ERole;
import com.urutare.stockm.repository.BlockedTokenRepository;
import com.urutare.stockm.repository.ResetRepository;
import com.urutare.stockm.repository.RoleRepository;
import com.urutare.stockm.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.security.auth.message.AuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final EmailService emailService;
    private final OathService oathService;
    private final Properties properties;
    private final UserRepository userRepository;
    private final ResetRepository resetRepository;
    private final RoleRepository roleRepository;
    private final BlockedTokenRepository blockedTokenRepository;

    public UserService(@Autowired UserRepository userRepository,
                       @Autowired EmailService emailService,
                       @Autowired OathService oathService,
                       @Autowired Properties properties,
                       @Autowired ResetRepository resetRepository,
                       RoleRepository roleRepository,
                       BlockedTokenRepository blockedTokenRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.oathService = oathService;
        this.properties = properties;
        this.resetRepository = resetRepository;
        this.roleRepository = roleRepository;
        this.blockedTokenRepository = blockedTokenRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    public User validateUser(String email, String password) throws AuthException {
        if (email != null)
            email = email.toLowerCase();
        try {
            User userFound = userRepository.findByEmailAddress(email);

            if (!BCrypt.checkpw(password, userFound.getPassword())) {
                throw new AuthException("Invalid email or password");
            }
            return userFound;

        } catch (Exception e) {
            throw new AuthException("Invalid email or password");
        }
    }

    public List<PublicUser> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new PublicUser(user.getId(), user.getEmail(), user.getFullName()))
                .collect(Collectors.toList());
    }

    private void validateEmail(String email) throws AuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if (email == null) {
            throw new AuthException("Email is required");
        }
        email = email.toLowerCase();
        if (!pattern.matcher(email).matches()) {
            throw new AuthException("Invalid email format");
        }
    }

    public User registerUser(User user, Set<String> strRoles) throws AuthException, MessagingException {
        if (user.getEmail() != null) {
            user.setEmail(user.getEmail().toLowerCase());
        }
        if (user.getUsername() == null) {
            user.setUsername(user.getEmail());
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AuthException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AuthException("Error: Email is already in use!");
        }

        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (ERole.valueOf(role)) {
                    case ADMIN:
                        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case MANAGER:
                        Role modRole = roleRepository.findByName(ERole.MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        User newUser = userRepository.save(user);
        sendEmailWithContext(newUser, newUser.getEmail(), "Welcome to Urutare Inc!", "signup_email.html");
        return newUser;
    }

    public User findById(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));
        return user;
    }

    public void forgotPassword(String email) throws AuthException, MessagingException {
        if (email == null) {
            throw new AuthException("Email is required");
        }
        email = email.toLowerCase();
        User user = userRepository.findByEmailAddress(email);
        if (user == null) {
            throw new AuthException("Email not found");
        }
        String token = oathService.generateResetPasswordToken(user);
        resetRepository.save(new ResetPasswordToken(user.getId(), token));
        String resetPasswordLink = "/api/auth/reset-password?token=" + token;
        String subject = "Reset your password";
        Context context = new Context();
        context.setVariable("link", properties.getBASE_URL() + resetPasswordLink);

        emailService.sendEmail(email, subject, context, "reset_password_email.html");
    }

    public void resetPassword(String token, String password) throws AuthException {
        ResetPasswordToken resetPasswordToken = resetRepository.findByToken(token);
        if (resetPasswordToken == null) {
            throw new AuthException("Invalid token");
        }

        User user = oathService.getUserFromTokenWhenResetPassword(token);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        userRepository.save(user);

        resetRepository.delete(resetPasswordToken);
    }

    public record PublicUser(Long id, String email, String fullName) {
    }

    public void logoutUser(String userId) {
        userRepository.updateIsActive(userId, false);
    }

    public void activateUser(String userId) {
        userRepository.updateIsActive(userId, true);
    }

    public Boolean isActive(String userId) {
        return userRepository.getIsActiveById(userId);
    }

    public void updateEmailForUser(Long userId, String newEmail) throws AuthException, MessagingException {
        User user = this.findById(userId);
        updateEmail(user, newEmail);
    }

    public void updateEmail(String oldEmail, String newEmail) throws AuthException, MessagingException {
        User user = userRepository.findByEmailAddress(oldEmail);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        updateEmail(user, newEmail);
    }

    private void updateEmail(User user, String newEmail) throws AuthException, MessagingException {
        validateEmail(newEmail);
        long count = userRepository.getCountByEmail(newEmail);
        if (count > 0) {
            throw new AuthException("Email already in use");
        }

        user.setEmail(newEmail);
        userRepository.save(user);
        sendEmailWithContext(user, newEmail, "Urutare Inc account Email change!", "update_user_email.html");

    }

    private void sendEmailWithContext(User user, String email, String subject, String templateFile)
            throws MessagingException {
        Context context = new Context();
        context.setVariable("fullName", user.getFullName());
        context.setVariable("login_link", properties.getBASE_URL() + "/api/auth/login");
        context.setVariable("supportEmail", "info@urutare.rw");
        context.setVariable("supportPhone", "+250 7888888");
        context.setVariable("email", email);
        context.setVariable("welcome_image", "/images/celebrate.png");
        emailService.sendEmail(email, subject, context, templateFile);

    }

    public void changePassword(Long userId, String oldPassword, String newPassword) throws MessagingException {
        User user = this.findById(userId);
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("The old password is incorrect");
        }
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        userRepository.save(user);
        sendEmailWithContext(user, user.getEmail(), "Urutare Inc account password change!",
                "change_password_email.html");

    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    public User findUserByBlockedToken(String token) {
        BlockedToken blockedToken = blockedTokenRepository.findByToken(token);

        if (blockedToken == null) {
            return null;
        }
        return blockedToken.getUser();
    }

    public void blockUser(BlockedToken blockedToken) {
        blockedTokenRepository.save(blockedToken);
    }

    public void CreateRole(Long loggedInUserId, AddRoleBody roleBody) throws ConflictException {

        if (roleRepository.existsByName(roleBody.getName().name())) {
            throw new ConflictException("Error: Role is already registered!");
        }
        Role role = new Role();
        role.setName(roleBody.getName());
        roleRepository.save(role);
    }

    public void assignRole(Long loggedInUserId, AssignRoleBody roleBody)
            throws ConflictException, ResourceNotFoundException {

        Optional<Role> roleOptional = roleRepository.findByName(roleBody.getName());

        if (roleOptional.isEmpty()) {
            throw new ResourceNotFoundException("Error: Role is not registered!");
        }

        Optional<User> userOptional = userRepository.findById(roleBody.getUserId());

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Error: user with id" + roleBody.getUserId() + "   is not registered!");
        }
        User user = userOptional.get();
        Set<Role> userRoles = user.getRoles();
        if (userRoles.contains(roleOptional.get())) {
            throw new ConflictException("Error: Role is already assigned to the user!");
        }
        userRoles.add(roleOptional.get());
        user.setRoles(userRoles);
        userRepository.save(user);
    }


}