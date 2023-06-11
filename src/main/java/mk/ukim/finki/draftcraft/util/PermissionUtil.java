package mk.ukim.finki.draftcraft.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.draftcraft.domain.exceptions.ForbiddenException;
import mk.ukim.finki.draftcraft.domain.exceptions.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

public final class PermissionUtil {

  private static final String COOKIE_NAME = "auth-token";

  public static void checkUserEditPermission(String username, Long id, String updatedEmail) {
    String loggedInUser = getUsername();
    if (!loggedInUser.equals(username)) {
      throw new ForbiddenException(loggedInUser, id);
    }
    if (!username.equals(updatedEmail)) {
      throw new ForbiddenException(loggedInUser);
    }
  }

  public static void checkUserUploadImagePermission(String username, Long id) {
    if (!getUsername().equals(username)) {
      throw new ForbiddenException(username, id);
    }
  }

  public static String getUsername() {
    Authentication auth = getAuthentication();
    if (auth != null && auth.getName() != null) {
      return auth.getName();
    } else {
      throw new UserNotFoundException();
    }
  }

  public static String getToken(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    String token = null;

    String cookieToken = getTokenFromCookies(cookies);
    if (cookieToken == null && header != null) {
      if (isNotEmpty(header) || header.startsWith("Bearer ")) {
        token = header.split(" ")[1].trim();
      }
    } else {
      token = cookieToken;
    }

    return token;
  }

  private static String getTokenFromCookies(Cookie[] cookies) {
    return cookies != null ?
        Arrays.stream(cookies)
            .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElse(null)
        : null;
  }

  private static Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

}
