package mk.ukim.finki.draftcraft.rest;

import mk.ukim.finki.draftcraft.domain.model.common.UrlToken;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.dto.UserDto;
import mk.ukim.finki.draftcraft.dto.input.user.ChangeUserPasswordDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateUserDto;
import mk.ukim.finki.draftcraft.dto.input.user.PasswordDto;
import mk.ukim.finki.draftcraft.dto.input.user.UpdateUserDto;
import mk.ukim.finki.draftcraft.repository.ImageRepository;
import mk.ukim.finki.draftcraft.repository.TokenRepository;
import mk.ukim.finki.draftcraft.repository.UserRepository;
import mk.ukim.finki.draftcraft.util.ImageUtil;
import mk.ukim.finki.draftcraft.utils.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersIT extends BaseIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  private TokenRepository tokenRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @Value("${application.url.token.expiration}")
  int minutes;

  @BeforeEach
  public void beforeEach() {
    tokenRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
//  @WithMockUser(roles = {ADMIN})
  void createUser() throws Exception {
    //given
    int sizeBefore = userRepository.findAll().size();

    CreateUserDto createUserDto = getCreateUserDto();

    //when
    MvcResult result = mockMvc.perform(post("/api/users")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(createUserDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value(createUserDto.getName()))
        .andExpect(jsonPath("$.surname").value(createUserDto.getSurname()))
        .andExpect(jsonPath("$.email").value(createUserDto.getEmail()))
        .andReturn();

    //then
    UserDto userDto = parseResponse(result, UserDto.class);

    List<User> usersList = userRepository.findAll();
    assertThat(usersList).isNotEmpty();
    assertThat(usersList).hasSize(sizeBefore + 1);
    User user = userRepository.findById(userDto.getId()).get();
    assertThat(user.getName()).isEqualTo(createUserDto.getName());
    assertThat(user.getSurname()).isEqualTo(createUserDto.getSurname());
    assertThat(user.getEmail()).isEqualTo(createUserDto.getEmail());
  }

  @Test
//  @WithMockUser(roles = {ADMIN})
  public void duplicateEmail() throws Exception {
    //given
    CreateUserDto createUserDto = getCreateUserDto();
    int sizeBefore = userRepository.findAll().size();

    //when
    mockMvc.perform(post("/api/users")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(createUserDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

    List<User> usersList = userRepository.findAll();
    assertThat(usersList).isNotEmpty();
    assertThat(usersList).hasSize(sizeBefore + 1);

    mockMvc.perform(post("/api/users")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(createUserDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
//  @WithMockUser(roles = {ADMIN})
  void listUsers() throws Exception {
    //given
    User user1 = generateRandomUser(false);
    userRepository.save(user1);
    User user2 = generateRandomUser(false);
    userRepository.save(user2);
    User user3 = generateRandomUser(false);
    userRepository.save(user3);
    int size = userRepository.findAllByOrderByNameAsc().size();

    //when & then
    mockMvc.perform(get("/api/users")
            .contentType("application/json")
            .param("size", String.valueOf(2)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(size)));
  }

  @Test
//  @WithMockUser(roles = {ADMIN})
  void getUser() throws Exception {
    //given
    User user = generateRandomUser(false);
//    user.setUserRole(Role.ADMIN);
    userRepository.save(user);

    //when & then
    mockMvc.perform(get("/api/users/" + user.getId())
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.id").value(user.getId()))
        .andExpect(jsonPath("$.name").value(user.getName()))
        .andExpect(jsonPath("$.surname").value(user.getSurname()))
        .andExpect(jsonPath("$.email").value(user.getEmail()));
  }

  @Test
//  @WithMockUser(username = "test@gmail.com", roles = {ADMIN, USER})
  @WithMockUser(username = "zdraveskaema@gmail.com")
  void getUserProfile() throws Exception {
    //given
    String email = "zdraveskaema@gmail.com";
    User user = generateRandomUser(false);
    user.setEmail(email);
    userRepository.save(user);

    //when & then
    mockMvc.perform(get("/api/users/me")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.id").value(user.getId()))
        .andExpect(jsonPath("$.name").value(user.getName()))
        .andExpect(jsonPath("$.surname").value(user.getSurname()))
        .andExpect(jsonPath("$.email").value(user.getEmail()));
  }

  @Test
//  @WithMockUser(roles = {ADMIN})
  @WithMockUser(username = "test.user@gmail.com")
  void editUser() throws Exception {
    //given
    User user = generateRandomUser(false);
    user.setEmail("test.user@gmail.com");
    userRepository.save(user);
    UpdateUserDto updateUserDto = getUpdateUserDto("test.user@gmail.com");

    int sizeBefore = userRepository.findAll().size();

    //when
    mockMvc.perform(put("/api/users/" + user.getId())
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(updateUserDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value(updateUserDto.getName()))
        .andExpect(jsonPath("$.surname").value(updateUserDto.getSurname()))
        .andExpect(jsonPath("$.email").value(updateUserDto.getEmail()));

    //then
    List<User> usersList = userRepository.findAll();
    assertThat(usersList).isNotEmpty();
    assertThat(usersList).hasSize(sizeBefore);

    User actual = userRepository.findById(user.getId()).get();
    assertThat(actual.getName()).isEqualTo(updateUserDto.getName());
    assertThat(actual.getSurname()).isEqualTo(updateUserDto.getSurname());
    assertThat(actual.getEmail()).isEqualTo(updateUserDto.getEmail());
  }

  @Test
//  @WithMockUser(roles = USER, username = "test@gmail.com")
  @WithMockUser(username = "zdraveskaema@gmail.com")
  void updateOwnProfile() throws Exception {
    //given
    User user = generateRandomUser(false);
    user.setEmail("zdraveskaema@gmail.com");
    userRepository.save(user);
    UpdateUserDto updateUserDto = getUpdateUserDto(user.getEmail());

    int sizeBefore = userRepository.findAll().size();

    //when
    mockMvc.perform(put("/api/users/" + user.getId())
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(updateUserDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value(updateUserDto.getName()))
        .andExpect(jsonPath("$.surname").value(updateUserDto.getSurname()))
        .andExpect(jsonPath("$.email").value(updateUserDto.getEmail()));

    //then
    List<User> usersList = userRepository.findAll();
    assertThat(usersList).isNotEmpty();
    assertThat(usersList).hasSize(sizeBefore);

    User actual = userRepository.findById(user.getId()).get();
    assertThat(actual.getName()).isEqualTo(updateUserDto.getName());
    assertThat(actual.getSurname()).isEqualTo(updateUserDto.getSurname());
    assertThat(actual.getEmail()).isEqualTo(updateUserDto.getEmail());
  }

  @Test
//  @WithMockUser(roles = {ADMIN, USER}, username = "test@gmail.com")
  @WithMockUser(username = "zdraveskaema@gmail.com")
  void changePassword() throws Exception {
    //given
    User user = generateRandomUser(false);
    String email = "zdraveskaema@gmail.com";
    String oldPassword = "oldPassword";
    user.setEmail(email);
    String encryptedOldPassword = passwordEncoder.encode(oldPassword);
    user.setPassword(encryptedOldPassword);
    userRepository.save(user);
    ChangeUserPasswordDto changeUserPasswordDto = getChangePasswordDto();

    //when
    mockMvc.perform(put("/api/users/change-password")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(changeUserPasswordDto)))
        .andExpect(status().isOk());

    //then
    User expected = userRepository.findByEmail(user.getEmail()).get();
    assertNotEquals(encryptedOldPassword, expected.getPassword());
  }

//  @Test
//  void requestResetPassword() throws Exception {
//    //given
//    User user = generateRandomUser(false);
//    String email = "zdraveskaema@gmail.com";
//    user.setEmail(email);
//    userRepository.save(user);
//
//    //when
//    mockMvc.perform(post("/api/users/request-reset-password")
//            .contentType("application/json")
//            .param("email", email))
//        .andExpect(status().isOk());
//
//    //then
//    var receivedMessages = smtpServer.getReceivedMessages();
//    assertEquals(1, receivedMessages.length);
//
//    assertTrue(tokenRepository.findByUser(user).isPresent());
//  }

  @Test
  void requestPassword() throws Exception {
    //given
    User existingUser = generateRandomUser(false);
    String email = "zdraveskaema@gmail.com";
    existingUser.setEmail(email);
    String oldPassword = existingUser.getPassword();
    userRepository.save(existingUser);

    PasswordDto passwordDto = getResetPasswordDto();

    UrlToken token = generateUrlToken(null, existingUser, minutes);
    tokenRepository.save(token);

    //when
    mockMvc.perform(put("/api/users/request-password")
            .contentType("application/json")
            .param("token", token.getToken())
            .content(objectMapper.writeValueAsString(passwordDto)))
        .andExpect(status().isOk());

    //then
    User updatedUser = userRepository.findByEmail(existingUser.getEmail()).get();
    assertNotEquals(oldPassword, updatedUser.getPassword());
  }

  @Test
//  @WithMockUser(roles = {ADMIN, USER}, username = "test@gmail.com")
  @WithMockUser(username = "zdraveskaema@gmail.com")
  void uploadImage() throws Exception {
    //given
    MockMultipartFile file = generateMultiPartFile();
    var image = ImageUtil.getImageFrom(file);
    imageRepository.save(image);
    User user = generateRandomUser(false);
    String email = "zdraveskaema@gmail.com";
    user.setEmail(email);
    user.setImage(image);
    userRepository.save(user);

    //when & then
    mockMvc.perform(multipart("/api/users/" + user.getId() + "/image")
            .file(file)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

}