package com.urutare.stockmuser.service;

import com.urutare.stockmuser.constants.Properties;
import com.urutare.stockmuser.dto.request.AddRoleBody;
import com.urutare.stockmuser.dto.request.AssignOrRemoveRoleBody;
import com.urutare.stockmuser.dto.request.ResetPasswordRequestBody;
import com.urutare.stockmuser.entity.BlockedToken;
import com.urutare.stockmuser.entity.ResetPasswordToken;
import com.urutare.stockmuser.entity.Role;
import com.urutare.stockmuser.entity.User;
import com.urutare.stockmuser.exception.ConflictException;
import com.urutare.stockmuser.exception.ResourceNotFoundException;
import com.urutare.stockmuser.models.ERole;
import com.urutare.stockmuser.repository.BlockedTokenRepository;
import com.urutare.stockmuser.repository.ResetRepository;
import com.urutare.stockmuser.repository.RoleRepository;
import com.urutare.stockmuser.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final EmailService emailService;
    private final OathService oathService;
    private final Properties properties;
    private final UserRepository userRepository;
    private final ResetRepository resetRepository;
    private final RoleRepository roleRepository;
    private final BlockedTokenRepository blockedTokenRepository;
    private final OTPService otpService;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if (username.contains("@")) {
            user = this.findByEmail(username);
        } else {
            user = this.findByPhoneNumber(username);
        }


        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(user.getId(),
                username,
                user.getPassword(),
                authorities,
                user.isEmailVerified(), user.isPhoneVerified());
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
                .map(user -> new PublicUser(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName()))
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
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AuthException("Email is already in use!");
        }

        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new AuthException("Phone number is already in use!");
        }

        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> roles.add(roleRepository.findByName(ERole.valueOf(role))
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."))));
        }

        user.setRoles(roles);
        User newUser = userRepository.save(user);
        sendEmailWithContext(newUser, newUser.getEmail(), "Welcome to Urutare Inc!", "signup_email.html");
        return newUser;
    }

    public User findById(UUID userId) throws ResourceNotFoundException {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));
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

    public User resetPassword(ResetPasswordRequestBody body) {

        if (!otpService.isOTPValid(body.getUsername(), body.getOtp())) {
            throw new ResourceNotFoundException("Invalid or OTP");
        }

        User user;
        if (body.getUsername().contains("@")) {
            user = this.findByEmail(body.getUsername().trim().toLowerCase());
        } else {
            user = this.findByPhoneNumber(body.getUsername().trim().toLowerCase());
        }
        String hashedPassword = BCrypt.hashpw(body.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashedPassword);

        otpService.deleteOTP(body.getUsername());

        return userRepository.save(user);
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
        context.setVariable("fullName", user.getFirstName() + " " + user.getLastName());
        context.setVariable("login_link", properties.getBASE_URL() + "/api/auth/login");
        context.setVariable("supportEmail", "info@urutare.rw");
        context.setVariable("supportPhone", "250 7888888");
        context.setVariable("email", email);
        context.setVariable("welcome_image", "/images/celebrate.png");
        emailService.sendEmail(email, subject, context, templateFile);

    }

    public void changePassword(UUID userId, String oldPassword, String newPassword) throws MessagingException {
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

    public void createRole(AddRoleBody roleBody) throws ConflictException {

        if (roleRepository.existsByName(roleBody.getName().name())) {
            throw new ConflictException("Error: Role is already registered!");
        }
        Role role = new Role();
        role.setName(roleBody.getName());
        roleRepository.save(role);
    }

    public void assignRole(AssignOrRemoveRoleBody roleBody)
            throws ConflictException, ResourceNotFoundException {
        Pair<Role, User> pair = validateRoleAndUser(roleBody);
        Role role = pair.getLeft();
        User user = pair.getRight();
        Set<Role> userRoles = user.getRoles();
        if (userRoles.contains(role)) {
            throw new ConflictException("Error: Role is already assigned to the user!");
        }
        userRoles.add(role);
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    private Pair<Role, User> validateRoleAndUser(AssignOrRemoveRoleBody roleBody) {
        Optional<Role> roleOptional = roleRepository.findByName(roleBody.getName());
        if (roleOptional.isEmpty()) {
            throw new ResourceNotFoundException("Error: Role is not registered!");
        }

        Optional<User> userOptional = userRepository.findById(roleBody.getUserId());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Error: user with id " + roleBody.getUserId() + " is not registered!");
        }

        return Pair.of(roleOptional.get(), userOptional.get());
    }

    public void removeRole(AssignOrRemoveRoleBody roleBody) throws ResourceNotFoundException {
        Pair<Role, User> pair = validateRoleAndUser(roleBody);
        Role role = pair.getLeft();
        User user = pair.getRight();
        Set<Role> userRoles = user.getRoles();
        if (!userRoles.contains(role)) {
            throw new ResourceNotFoundException("Error: Role is not assigned to the user!");
        }
        userRoles.remove(role);
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with email: " + email));
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with phone number: " + phoneNumber));
    }


    public User findByID(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with id: " + userId));
    }

    public record PublicUser(UUID id, String email, String firstName, String lastName) {
    }

}