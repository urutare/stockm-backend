package com.urutare.stockm.service;

import com.urutare.stockm.constants.Properties;
import com.urutare.stockm.dto.UserDto;
import com.urutare.stockm.entity.ResetPasswordToken;
import com.urutare.stockm.entity.User;
import com.urutare.stockm.exception.ResourceNotFoundException;
import com.urutare.stockm.repository.ResetRepository;
import com.urutare.stockm.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.security.auth.message.AuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserService {

    private final EmailService emailService;
    private final OathService oathService;
    private final Properties properties;
    private final UserRepository userRepository;
    private final ResetRepository resetRepository;


    public UserService(@Autowired UserRepository userRepository,
                       @Autowired EmailService emailService,
                       @Autowired OathService oathService,
                       @Autowired Properties properties,
                       @Autowired ResetRepository resetRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.oathService = oathService;
        this.properties = properties;
        this.resetRepository = resetRepository;
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
        if (email == null){
            throw new AuthException("Email is required");
        }
        email = email.toLowerCase();
        if (!pattern.matcher(email).matches()){
            throw new AuthException("Invalid email format");
        }
    }

    public User registerUser(User user) throws AuthException {
        String email = user.getEmail();
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        user.setActive(false);
        validateEmail(email);
        long count = userRepository.getCountByEmail(email);
        if (count > 0)
            throw new AuthException("Email already in use");
        emailService.sendEmail(email, "Welcome to Urutare Inc!", "Dear " + user.getFullName() + ",\n\nWe are thrilled to welcome you to Urutare Inc! Thank you for creating an account with our stock management app. We're confident that you'll find our app to be a powerful tool for managing your inventory and streamlining your business operations.\n\nAs a new member of our community, you can expect exceptional customer support and ongoing improvements to our app. We're committed to providing you with the best possible experience.\n\nIf you have any questions or concerns, please don't hesitate to reach out to our support team at info@urutare.rw.\n\nThank you again for choosing Urutare Inc. We look forward to helping your business thrive!\n\nBest regards,\nUrutare Inc.");

        return userRepository.save(user);
    }

    public UserDto findById(String userId) throws ResourceNotFoundException {
        UserDto userDto = userRepository.findUserDtoById(userId);
        if (userDto == null)
            throw new ResourceNotFoundException("User not found");
        return userDto;
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
    public void activateUser(String userId){
        userRepository.updateIsActive(userId, true);
    }
    public Boolean isActive(String userId){
        return userRepository.getIsActiveById(userId);
    }
    public void updateEmail(String userId, String newEmail) throws AuthException {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        validateEmail(newEmail);
        long count = userRepository.getCountByEmail(newEmail);
        if (count > 0){
            throw new AuthException("Email already in use");
        }

        user.setEmail(newEmail);
        userRepository.save(user);
        emailService.sendEmail(newEmail, "Welcome to Urutare Inc!", "Dear " + user.getFullName() + ",\n\nWe are thrilled to welcome you to Urutare Inc with your new email address! .\n\n feel free to request an exceptional  support whenever you need. We're committed to providing you with the best possible experience.\n\nIf you have any questions or concerns, please don't hesitate to reach out to our support team at info@urutare.rw.\n\nThank you again for choosing Urutare Inc. We look forward to helping your business thrive!\n\nBest regards,\nUrutare Inc.");

    }

}