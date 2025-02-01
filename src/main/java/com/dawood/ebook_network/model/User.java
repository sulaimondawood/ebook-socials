package com.dawood.ebook_network.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Long id;
  private String fullname;
  @Column(unique = true)
  private String email;
  private String password;

  private LocalDate dob;
  private boolean accountLocked;
  private boolean enabled;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Role> roles;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdDate;
  @LastModifiedDate
  @Column(insertable = false)
  private LocalDateTime lastModifiedAt;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream().map(role->new SimpleGrantedAuthority(role.getRole())).toList();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return !accountLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
