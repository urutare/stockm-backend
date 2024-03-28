package com.urutare.stockmuser.service;

import com.urutare.stockmuser.entity.OTP;
import com.urutare.stockmuser.entity.User;
import com.urutare.stockmuser.exception.ResourceNotFoundException;
import com.urutare.stockmuser.repository.OTPRepository;
import com.urutare.stockmuser.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

@Service
@Transactional
@AllArgsConstructor
public class OTPService {

    private OTPRepository otpRepository;
    private UserRepository userRepository;
    private EmailService emailService;
    private SmsService smsService;

    public String generateOTP(String emailOrPhone) throws MessagingException {
        OTP existingOTP = otpRepository.findByUsername(emailOrPhone);

        if (existingOTP != null) {
            // Delete existing OTP
            otpRepository.delete(existingOTP);
        }

        // Generate OTP
        String otp = generateRandomOTP();

        Instant currentTime = Instant.now();
        long currentMillis = currentTime.toEpochMilli();

        OTP newOTP = new OTP();
        newOTP.setUsername(emailOrPhone);
        newOTP.setOtpCode(otp);
        newOTP.setCreatedAt(new Timestamp(currentMillis));
        otpRepository.save(newOTP);

        User user = null;

        if (emailOrPhone.contains("@")) {
            user = userRepository.findByEmail(emailOrPhone).orElseThrow(
                    () -> new ResourceNotFoundException("User not found with email: " + emailOrPhone));
            Context context = new Context();
            context.setVariable("fullName", user.getFirstName() + " " + user.getLastName());
            context.setVariable("supportEmail", "info@urutare.com");
            context.setVariable("supportPhone", "250 7888888");
            context.setVariable("otp", otp);
            context.setVariable("welcome_image", "/images/celebrate.png");
            emailService.sendEmail(user.getEmail(), "User OTP", context, "otp_email.html");
        } else {
            user = userRepository.findByPhoneNumber(emailOrPhone).orElseThrow(
                    () -> new ResourceNotFoundException("User not found with phone: " + emailOrPhone));
            smsService.sendSms(user.getPhoneNumber(), "OTP is: " + otp);

        }


        return "OTP generated successfully and sent to " + emailOrPhone;
    }

    public boolean isOTPValid(String emailOrPhone, String otp) {
        OTP otpEntity = otpRepository.findByUsernameAndOtpCode(emailOrPhone, otp);
        if (otpEntity == null) {
            return false;
        }

        // Validate OTP
        if (!otp.equals(otpEntity.getOtpCode())) {
            return false;
        }

        // Check if OTP has expired
        // 1 minute in milliseconds
        int OTP_EXPIRY_TIME = 60000;
        Instant currentTime = Instant.now();
        long currentMillis = currentTime.toEpochMilli();
        long otpCreationTime = otpEntity.getCreatedAt().getTime();
        long timeElapsed = currentMillis - otpCreationTime;

        return timeElapsed <= OTP_EXPIRY_TIME;
    }

    public String verifyOTP(String emailOrPhone, String otp) {
        OTP otpEntity = otpRepository.findByUsernameAndOtpCode(emailOrPhone, otp);
        if (otpEntity == null) {
            throw new ResourceNotFoundException("OTP not found. Please request a new one.");
        }

        // Validate OTP
        if (!otp.equals(otpEntity.getOtpCode())) {
            throw new ResourceNotFoundException("Incorrect OTP. Please try again.");
        }

        // Check if OTP has expired
        // 5 minutes in milliseconds
        int OTP_EXPIRY_TIME = 300000;
        Instant currentTime = Instant.now();
        long currentMillis = currentTime.toEpochMilli();
        long otpCreationTime = otpEntity.getCreatedAt().getTime();
        long timeElapsed = currentMillis - otpCreationTime;

        if (timeElapsed > OTP_EXPIRY_TIME) {
            throw new ResourceNotFoundException("OTP has expired. Please request a new one.");
        }


        User user;
        if (emailOrPhone.contains("@")) {
            user = userRepository.findByEmail(emailOrPhone).orElseThrow(
                    () -> new ResourceNotFoundException("User not found with email: " + emailOrPhone));
            user.setEmailVerified(true);
        } else {
            user = userRepository.findByPhoneNumber(emailOrPhone).orElseThrow(
                    () -> new ResourceNotFoundException("User not found with phone: " + emailOrPhone));
            user.setPhoneVerified(true);
        }
        userRepository.save(user);

        // Delete OTP after successful verification
        otpRepository.delete(otpEntity);

        // Perform further actions like directing user to confirmation page or activating account
        return "OTP verified successfully for " + emailOrPhone;
    }

    private String generateRandomOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a random 6-digit number
        return String.valueOf(otp);
    }

    public void deleteOTP(String username) {
        OTP existingOTP = otpRepository.findByUsername(username);
        if (existingOTP != null) {
            otpRepository.delete(existingOTP);
        }
    }
}
