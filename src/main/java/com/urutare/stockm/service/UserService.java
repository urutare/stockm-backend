package com.urutare.stockm.service;

import com.urutare.stockm.dto.UserDto;
import com.urutare.stockm.entity.User;
import com.urutare.stockm.exception.ResourceNotFoundException;
import com.urutare.stockm.repository.UserRepository;
import jakarta.security.auth.message.AuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserService {

    UserRepository userRepository;
   public UserService(UserRepository userRepository){
       this.userRepository=userRepository;
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
    public record PublicUser(Long id, String email, String fullName) {}
    public List<PublicUser> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<PublicUser> publicUsers = users.stream()
                .map(user -> new PublicUser(user.getId(), user.getEmail(), user.getFullName()))
                .collect(Collectors.toList());
        return publicUsers;
    }


    public User registerUser(User user) throws AuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        String email = user.getEmail();
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        if (email != null)
            email = email.toLowerCase();
        if (!pattern.matcher(email).matches())
            throw new AuthException("Invalid email format");
        long count = userRepository.getCountByEmail(email);
        if (count > 0)
            throw new AuthException("Email already in use");
        return userRepository.save(user);
    }

    public UserDto findById(String userId) throws ResourceNotFoundException {
        UserDto userDto = userRepository.findUserDtoById(userId);
        if (userDto == null)
            throw new ResourceNotFoundException("User not found");
        return userDto;
    }

}