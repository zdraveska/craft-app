//package mk.ukim.finki.draftcraft.security.jwt;
//
//import lombok.extern.slf4j.Slf4j;
//import net.jodah.expiringmap.ExpiringMap;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Component;
//
//import java.time.Instant;
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@Component
//public class JwtTokenCache {
//
//  private final ExpiringMap<String, String> tokenEventMap;
//  private final JwtTokenUtil jwtTokenUtil;
//
//  public JwtTokenCache(@Lazy JwtTokenUtil jwtTokenUtil) {
//    this.jwtTokenUtil = jwtTokenUtil;
//    this.tokenEventMap = ExpiringMap.builder()
//        .variableExpiration()
//        .maxSize(1000)
//        .build();
//  }
//
//  public void markUserAsLoggedOut(String token, String username) {
//
//    Date tokenExpiryDate = jwtTokenUtil.getTokenExpiryFromJWT(token);
//    long ttlForToken = getTTLForToken(tokenExpiryDate);
//
//    log.debug("Logout token cache set for {} with a TTL of {} seconds. Token is due expiry at {}", username,
//        ttlForToken,
//        tokenExpiryDate);
//    tokenEventMap.put(token, username, ttlForToken, TimeUnit.SECONDS);
//  }
//
//  public Boolean checkIfEventMapContainToken(String token) {
//    return tokenEventMap.containsKey(token);
//  }
//
//  private long getTTLForToken(Date date) {
//    long secondAtExpiry = date.toInstant().getEpochSecond();
//    long secondAtLogout = Instant.now().getEpochSecond();
//    return Math.max(0, secondAtExpiry - secondAtLogout);
//  }
//
//}
