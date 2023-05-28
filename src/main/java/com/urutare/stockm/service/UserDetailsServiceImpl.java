package com.urutare.stockm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urutare.stockm.entity.User;
import com.urutare.stockm.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  UserRepository userRepository;
public UserDetailsServiceImpl(UserRepository userRepository){
  this.userRepository=userRepository;
}
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmailOrUsername(username, username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

}
