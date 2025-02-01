package com.dawood.ebook_network.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfg {

  private final UserDetailsService userDetailsServiceImpl;

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider (){
    DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
    dao.setPasswordEncoder(passwordEncoder());
    dao.setUserDetailsService(userDetailsServiceImpl);
    return dao;
  }

}
