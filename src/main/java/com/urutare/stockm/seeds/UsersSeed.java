package com.urutare.stockm.seeds;

import com.urutare.stockm.entity.User;
import com.urutare.stockm.models.Role;
import com.urutare.stockm.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersSeed {
    UserRepository userRepository;

    public UsersSeed(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @PostConstruct
//    public void loadData() {
//        userRepository.saveAll(List.of(
//                new User("paterne@gmail.com", BCrypt.hashpw("password", BCrypt.gensalt(10)),
//                        "NDATUMUREMYI  Paterne", "0786388768", Role.admin)
//        ));
//    }
}
