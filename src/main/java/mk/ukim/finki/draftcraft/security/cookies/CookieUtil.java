package mk.ukim.finki.draftcraft.security.cookies;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.draftcraft.domain.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@Component
public class CookieUtil {

  private static final String COOKIE_NAME = "auth-token";

  private static final String REFRESH_COOKIE_NAME = "refresh-token";

  @Value("${application.security.cookie.expiration}")
  private int expiration;

  @Value("${application.security.cookie.secure}")
  private boolean secure;

  @Value("${application.url.baseurl}")
  private String baseUrl;

  public Cookie createTokenCookie(String token) {
    Cookie cookie = new Cookie(COOKIE_NAME, token);
    cookie.setSecure(secure);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(expiration);
    cookie.setPath("/");
    cookie.setDomain(getDomain(baseUrl));
    return cookie;
  }

  public Cookie createRefreshTokenCookie(String refreshToken) {
    Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, refreshToken);
    cookie.setSecure(secure);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(expiration);
    cookie.setPath("/");
    cookie.setDomain(getDomain(baseUrl));
    return cookie;
  }

  public Cookie deleteTokenCookie() {
    Cookie cookie = new Cookie(COOKIE_NAME, null);
    cookie.setSecure(secure);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    cookie.setDomain(getDomain(baseUrl));
    return cookie;
  }

  public Cookie deleteRefreshTokenCookie() {
    Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, null);
    cookie.setSecure(secure);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    cookie.setDomain(getDomain(baseUrl));
    return cookie;
  }

  private String getDomain(String urlString) {
    try {
      URL url = new URL(urlString);
      return url.getHost();
    } catch (MalformedURLException e) {
      log.error("Cannot parse domain from URL");
      throw new CustomException("Cannot parse domain from URL");
    }
  }

}