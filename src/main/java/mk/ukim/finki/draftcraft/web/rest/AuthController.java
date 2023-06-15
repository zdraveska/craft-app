package mk.ukim.finki.draftcraft.web.rest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.draftcraft.domain.exceptions.TokenIsNotValidException;
import mk.ukim.finki.draftcraft.domain.exceptions.UserNotFoundException;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.dto.LoginResponseDto;
import mk.ukim.finki.draftcraft.dto.input.user.LoginRequestDto;
import mk.ukim.finki.draftcraft.mapper.UserMapper;
import mk.ukim.finki.draftcraft.repository.UserRepository;
import mk.ukim.finki.draftcraft.security.cookies.CookieUtil;
import mk.ukim.finki.draftcraft.security.jwt.JwtService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    //    private final JwtTokenUtil jwtTokenUtil;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(HttpServletResponse response, @Valid @RequestBody LoginRequestDto loginRequest) throws Exception {
        try {
            var authenticate =authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = (User) authenticate.getPrincipal();
            String token = jwtService.generateToken(user);
            String refresh = jwtService.generateRefreshToken(user);

            response.addCookie(cookieUtil.createTokenCookie(token));
            response.addCookie(cookieUtil.createRefreshTokenCookie(refresh));

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(userMapper.toLoginResponseDto(userMapper.toDto(user), user.getRole()));
        } catch (BadCredentialsException ex) {
           throw new BadCredentialsException(ex.getMessage());
        }
    }

    //TODO
    @CacheEvict(cacheNames = {"user"}, allEntries = true)
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletResponse response) {
        Cookie tokenCookie = cookieUtil.deleteTokenCookie();
        Cookie refreshTokenCookie = cookieUtil.deleteRefreshTokenCookie();
        response.addCookie(tokenCookie);
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok().build();
    }

    //TODO MI TREBA?
    //    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(
            @CookieValue(name = "refresh-token", required = false) String refreshToken, HttpServletResponse response) {
        if (!jwtService.validate(refreshToken)) {
            throw new TokenIsNotValidException();
        }
        String username = jwtService.extractUserName(refreshToken);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException(username));
        String newToken = jwtService.generateToken(user);
        response.addCookie(cookieUtil.createTokenCookie(newToken));
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, newToken)
                .body(userMapper.toLoginResponseDto(userMapper.toDto(user), user.getRole()));
    }

}