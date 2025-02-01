package com.dawood.ebook_network.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  @Value("${JWT_EXPIRATION}")
  private long jwtExpiration;

  @Value("${JWT_SECRET}")
  private String SECRET;

  public String generateToken(UserDetails userDetails){
    return generateToken(new HashMap<String, Object>(), userDetails);
  }

  private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
    var authorities = userDetails.getAuthorities()
          .stream()
          .map(GrantedAuthority::getAuthority)
          .toList();

    return Jwts
          .builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis()+ jwtExpiration))
        .claim("authorities", authorities)
        .signWith(getSigningKey())
        .compact();
  }

  public boolean isTokenValid(String jwt, UserDetails userDetails){
    String username = extractUsername(jwt);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
  }

  private boolean isTokenExpired(String jwt){
    return extractClaim(jwt,(claim)->claim.getExpiration()).before(new Date());
  }

  public String extractUsername(String jwt){
    return extractClaim(jwt, (claim)-> claim.getSubject());
  }

  private <T> T extractClaim(String jwt, Function<Claims,T> claimsFunction){
    final Claims claims = extractAllClaims(jwt);
    return claimsFunction.apply(claims);
  }

  private Claims extractAllClaims (String jwt){
    return Jwts
        .parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(jwt)
        .getPayload();
  }

  private SecretKey getSigningKey() {
    byte[] key = Decoders.BASE64.decode(SECRET);
    return Keys.hmacShaKeyFor(key);
  }

}
