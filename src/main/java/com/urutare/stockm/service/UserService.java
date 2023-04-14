package com.urutare.stockm.service;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import com.urutare.stockm.entity.User;
import com.urutare.stockm.repository.UserRepository;
import jakarta.security.auth.message.AuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

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

}