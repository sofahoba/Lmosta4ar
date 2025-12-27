package com.fullDetailed.fullDetailedDemo.config.securityServices;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fullDetailed.fullDetailedDemo.domain.entities.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails{

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if(user.getRole()==null){
      return Collections.emptyList();
    }
    else{
      String role= "ROLE_"+user.getRole().name();
      return List.of(new SimpleGrantedAuthority(role));
    }
  }

  @Override
  public String getPassword(){
    return user.getPassword();
  }

  @Override
  public String getUsername(){
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public UUID getId() {
    return user.getId();
  }

  
  
}
