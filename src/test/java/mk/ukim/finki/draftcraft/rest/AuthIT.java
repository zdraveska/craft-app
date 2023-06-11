//package mk.ukim.finki.draftcraft.rest;
//
//import mk.ukim.finki.draftcraft.domain.User;
//import mk.ukim.finki.draftcraft.dto.LoginResponseDto;
//import mk.ukim.finki.draftcraft.dto.UserDto;
//import mk.ukim.finki.draftcraft.dto.input.LoginRequestDto;
//import mk.ukim.finki.draftcraft.repository.UserRepository;
//import mk.ukim.finki.draftcraft.security.jwt.JwtTokenUtil;
//import mk.ukim.finki.draftcraft.utils.BaseIntegrationTest;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MvcResult;
//
//import javax.servlet.http.Cookie;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class AuthIT extends BaseIntegrationTest {
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @Autowired
//  private JwtTokenUtil jwtTokenUtil;
//
//  @Autowired
//  private PasswordEncoder passwordEncoder;
//
//  private static final String COOKIE_NAME = "auth-token";
//
//  private static final String REFRESH_COOKIE_NAME = "refresh-token";
//
//  @Test
//  public void login() throws Exception {
//    //given
//    User user = generateRandomUser(false);
//    String password = "password";
//    user.setPassword(passwordEncoder.encode(password));
//    userRepository.save(user);
//
//    LoginRequestDto authRequestDto = getLoginRequestDto(user.getUsername(), password);
//
//    //when
//    MvcResult result = mockMvc.perform(post("/api/login")
//        .contentType("application/json")
//        .content(objectMapper.writeValueAsString(authRequestDto)))
//        .andExpect(status().isOk()).andReturn();
//
//    //then
//    LoginResponseDto loginResponseDto = parseResponse(result, LoginResponseDto.class);
//    assertNotNull(loginResponseDto);
//    UserDto userDto = loginResponseDto.getUserDto();
//    Cookie cookie = result.getResponse().getCookie(COOKIE_NAME);
//
//    assertNotNull(userDto);
//    assertThat(userDto.getName()).isEqualTo(user.getName());
//    assertThat(userDto.getSurname()).isEqualTo(user.getSurname());
//    assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
//    assertThat(loginResponseDto.getRole()).isEqualTo(user.getUserRole());
//    assertNotNull(cookie);
//    assertThat(jwtTokenUtil.getUsername(cookie.getValue())).isEqualTo(user.getEmail());
//  }
//
//  @Test
//  public void logout() throws Exception {
//    //given
//    User user = generateRandomUser(false);
//    String password = "password";
//    user.setPassword(passwordEncoder.encode(password));
//    userRepository.save(user);
//
//    LoginRequestDto authRequestDto = getLoginRequestDto(user.getUsername(), password);
//
//    //when & then
//    MvcResult result = mockMvc.perform(post("/api/login")
//        .contentType("application/json")
//        .content(objectMapper.writeValueAsString(authRequestDto)))
//        .andExpect(status().isOk()).andReturn();
//
//    String token = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
//    Cookie tokenCookie = result.getResponse().getCookie(COOKIE_NAME);
//    Cookie refreshTokenCookie = result.getResponse().getCookie(REFRESH_COOKIE_NAME);
//
//    mockMvc.perform(post("/api/logout")
//        .contentType("application/json")
//        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//        .cookie(tokenCookie,refreshTokenCookie))
//        .andExpect(status().isOk());
//
//    mockMvc.perform(get("/api/users")
//        .contentType("application/json")
//        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//        .cookie(tokenCookie, refreshTokenCookie))
//        .andExpect(status().isUnauthorized());
//  }
//
//  @Test
//  public void refreshToken() throws Exception {
//    //given
//    User user = generateRandomUser(false);
//    String password = "password";
//    user.setPassword(passwordEncoder.encode(password));
//    userRepository.save(user);
//
//    LoginRequestDto authRequestDto = getLoginRequestDto(user.getUsername(), password);
//
//    //when
//    MvcResult resultLogin = mockMvc.perform(post("/api/login")
//        .contentType("application/json")
//        .content(objectMapper.writeValueAsString(authRequestDto)))
//        .andExpect(status().isOk())
//        .andReturn();
//
//    String token = resultLogin.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
//    Cookie tokenCookie = resultLogin.getResponse().getCookie(COOKIE_NAME);
//    Cookie refreshTokenCookie = resultLogin.getResponse().getCookie(REFRESH_COOKIE_NAME);
//
//    Thread.sleep(1000);
//
//    MvcResult resultRefresh = mockMvc.perform(post("/api/refresh")
//        .contentType("application/json")
//        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//        .cookie(tokenCookie, refreshTokenCookie))
//        .andExpect(status().isOk())
//        .andReturn();
//
//    Cookie newTokenCookie = resultRefresh.getResponse().getCookie(COOKIE_NAME);
//
//    //then
//    assertThat(refreshTokenCookie).isNotNull();
//    assertThat(tokenCookie).isNotNull();
//    assertThat(newTokenCookie).isNotNull();
//    String newToken = newTokenCookie.getValue();
//    assertThat(token).isNotEqualTo(newToken);
//  }
//
//}
