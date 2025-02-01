package com.dawood.ebook_network.config;


import com.dawood.ebook_network.service.security.JwtService;
import com.dawood.ebook_network.service.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                @NonNull  FilterChain filterChain) throws ServletException, IOException {

    if(request.getServletPath().contains("/api/v1/auth")){
      filterChain.doFilter(request, response);
      return;
    }

    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String jwt;

    if(authHeader == null || !authHeader.startsWith("Bearer ")){
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7);
    String userEmail = jwtService.extractUsername(jwt);

    if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
      UserDetails user = userDetailsService.loadUserByUsername(userEmail);

      if(jwtService.isTokenValid(jwt,user)){
        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
        authenticate.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
      }

      filterChain.doFilter(request, response);
    }
  }
}
