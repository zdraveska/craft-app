//package mk.ukim.finki.draftcraft.security.jwt;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import mk.ukim.finki.draftcraft.service.UserService;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//import static java.util.Collections.emptyList;
//import static java.util.Optional.ofNullable;
//import static mk.ukim.finki.draftcraft.util.PermissionUtil.getToken;
//
//@Component
//@RequiredArgsConstructor
//public class JwtTokenFilter extends OncePerRequestFilter {
//
//  private final JwtTokenUtil jwtTokenUtil;
//
//  private final UserService userService;
//
//  @Override
//  protected void doFilterInternal(HttpServletRequest request,
//                                  HttpServletResponse response,
//                                  FilterChain chain) throws ServletException, IOException {
//    String token = getToken(request);
//
//    if (!jwtTokenUtil.validate(token)) {
//      chain.doFilter(request, response);
//      return;
//    }
//
//    UserDetails userDetails = userService
//        .findByEmail(jwtTokenUtil.getUsername(token))
//        .orElse(null);
//    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//        userDetails, null,
//        ofNullable(userDetails).map(UserDetails::getAuthorities).orElse(emptyList())
//    );
//
//    authentication
//        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//    chain.doFilter(request, response);
//  }
//
//}