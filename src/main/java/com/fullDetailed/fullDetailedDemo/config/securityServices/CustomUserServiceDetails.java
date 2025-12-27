package com.fullDetailed.fullDetailedDemo.config.securityServices;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserServiceDetails implements UserDetailsService {

  private final UserRepo userRepo;
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user=userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("user not found"));
    return new CustomUserDetails(user);
  }
  
}
