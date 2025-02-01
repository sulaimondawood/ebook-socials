package com.dawood.ebook_network.repository;

import com.dawood.ebook_network.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findByToken(String role);
}
