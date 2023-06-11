//package mk.ukim.finki.draftcraft.security.jwt;
//
//import io.jsonwebtoken.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class JwtTokenUtil {
//
//  @Value("${application.security.token.secret}")
//  private String jwtSecret;
//
//  @Value("${application.security.token.expiration}")
//  private int tokenExpiration;
//
//  @Value("${application.security.token.refresh.expiration}")
//  private int refreshExpiration;
//
//  private final JwtTokenCache jwtTokenCache;
//
//  public String generateToken(UserDetails userDetails) {
//    Map<String, Object> claims = new HashMap<>();
//    return createToken(claims, userDetails.getUsername(), tokenExpiration);
//  }
//
//  public String generateRefreshToken(UserDetails userDetails) {
//    Map<String, Object> claims = new HashMap<>();
//    return createToken(claims, userDetails.getUsername(), refreshExpiration);
//  }
//
//  private String createToken(Map<String, Object> claims, String subject, int expiration) {
//    return Jwts.builder()
//        .setClaims(claims)
//        .setSubject(subject)
//        .setIssuedAt(new Date(System.currentTimeMillis()))
//        .setExpiration(new Date(System.currentTimeMillis() + expiration))
//        .signWith(SignatureAlgorithm.HS256, jwtSecret)
//        .compact();
//  }
//
//  public String getUsername(String token) {
//    Claims claims = Jwts.parser()
//        .setSigningKey(jwtSecret)
//        .parseClaimsJws(token)
//        .getBody();
//
//    return claims.getSubject();
//  }
//
//  public boolean validate(String token) {
//    try {
//      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
//      return !jwtTokenCache.checkIfEventMapContainToken(token);
//    } catch (SignatureException ex) {
//      log.error("Invalid JWT signature - {}", ex.getMessage());
//    } catch (MalformedJwtException ex) {
//      log.error("Invalid JWT token - {}", ex.getMessage());
//    } catch (ExpiredJwtException ex) {
//      log.error("Expired JWT token - {}", ex.getMessage());
//    } catch (UnsupportedJwtException ex) {
//      log.error("Unsupported JWT token - {}", ex.getMessage());
//    } catch (IllegalArgumentException ex) {
//      log.error("JWT claims string is empty - {}", ex.getMessage());
//    }
//    return false;
//  }
//
//  public Date getTokenExpiryFromJWT(String token) {
//    Claims claims = Jwts.parser()
//        .setSigningKey(jwtSecret)
//        .parseClaimsJws(token)
//        .getBody();
//
//    return claims.getExpiration();
//  }
//
//}