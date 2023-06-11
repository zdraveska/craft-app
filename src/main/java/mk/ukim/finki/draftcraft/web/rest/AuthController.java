package mk.ukim.finki.draftcraft.web.rest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.draftcraft.domain.users.Name;
import mk.ukim.finki.draftcraft.domain.users.User;
import mk.ukim.finki.draftcraft.domain.users.UserRole;
import mk.ukim.finki.draftcraft.dto.LoginResponseDto;
import mk.ukim.finki.draftcraft.dto.input.user.LoginRequestDto;
import mk.ukim.finki.draftcraft.mapper.UserMapper;
import mk.ukim.finki.draftcraft.repository.UserRepository;
import mk.ukim.finki.draftcraft.security.cookies.CookieUtil;
import mk.ukim.finki.draftcraft.security.jwt.JwtService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<User>> users() {
        User user = User.builder()
                .email("ema.zdraveska@valtech.com")
                .name(new Name("Ema", "Zdr"))
                .phoneNumber("071292523")
                .username("ema")
                .password(passwordEncoder.encode("pass"))
                .userRole(UserRole.BUYER)
                .build();
        userRepository.save(user);
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(HttpServletResponse response, @Valid @RequestBody LoginRequestDto loginRequest) {
//        User saveUser = User.builder()
//                .email("ema.zdraveska@valtech.com")
//                .name(new Name("Ema", "Zdr"))
//                .phoneNumber("071292523")
//                .username("ema")
//                .password(passwordEncoder.encode("pass"))
//                .userRole(UserRole.BUYER)
//                .build();
//        userRepository.save(saveUser);
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtService.generateToken(user);
            String refresh = jwtService.generateRefreshToken(user);

            response.addCookie(cookieUtil.createTokenCookie(token));
            response.addCookie(cookieUtil.createRefreshTokenCookie(refresh));


//      UserDto dto = UserDto.builder()
//              .image(user.getImage())
//              .id(user.getId())
//              .email(user.getEmail())
//              .surname()
//              .build()
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(userMapper.toLoginResponseDto(userMapper.toDto(user), user.getUserRole()));
        } catch (BadCredentialsException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

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

//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @PostMapping("/refresh")
//    public ResponseEntity<LoginResponseDto> refreshToken(
//            @CookieValue(name = "refresh-token", required = false) String refreshToken, HttpServletResponse response) {
//        if (!jwtTokenUtil.validate(refreshToken)) {
//            throw new TokenIsNotValidException();
//        }
//        String username = jwtTokenUtil.getUsername(refreshToken);
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
//        String newToken = jwtTokenUtil.generateToken(user);
//        response.addCookie(cookieUtil.createTokenCookie(newToken));
//        return ResponseEntity.ok()
//                .header(HttpHeaders.AUTHORIZATION, newToken)
//                .body(userMapper.toLoginResponseDto(userMapper.toDto(user), user.getUserRole()));
//    }

}