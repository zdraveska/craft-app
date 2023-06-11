//package mk.ukim.finki.draftcraft.impl;
//
//import mk.ukim.finki.draftcraft.domain.UrlToken;
//import mk.ukim.finki.draftcraft.domain.User;
//import mk.ukim.finki.draftcraft.domain.enumerations.EmailType;
//import mk.ukim.finki.draftcraft.domain.enumerations.Role;
//import mk.ukim.finki.draftcraft.domain.enumerations.UserStatus;
//import mk.ukim.finki.draftcraft.domain.exceptions.*;
//import mk.ukim.finki.draftcraft.dto.EmailDto;
//import mk.ukim.finki.draftcraft.dto.UrlTokenDto;
//import mk.ukim.finki.draftcraft.dto.UserDto;
//import mk.ukim.finki.draftcraft.dto.input.ChangeUserPasswordDto;
//import mk.ukim.finki.draftcraft.dto.input.CreateUserDto;
//import mk.ukim.finki.draftcraft.dto.input.PasswordDto;
//import mk.ukim.finki.draftcraft.dto.input.UpdateUserDto;
//import mk.ukim.finki.draftcraft.repository.UserRepository;
//import mk.ukim.finki.draftcraft.service.UserService;
//import mk.ukim.finki.draftcraft.service.mapper.UserMapper;
//import mk.ukim.finki.draftcraft.utils.BaseServiceTest;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mapstruct.factory.Mappers;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.data.domain.Pageable;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//public class UserServiceTest extends BaseServiceTest {
//
//  @Mock
//  UserRepository userRepository;
//
//  @Mock
//  PasswordEncoder passwordEncoder;
//
//  UserMapper userMapper = Mappers.getMapper(UserMapper.class);
//
//  @Mock
//  UserService userService;
//
//  @Captor
//  ArgumentCaptor<EmailDto> emailCaptor;
//
//  private final int URL_EXPIRATION = 1440;
//
//  @BeforeEach
//  void setup() {
//    userService = new UserServiceImpl(userRepository, userMapper, emailService, passwordEncoder,
//        tokenService);
//  }
//
//  @Test
//  @WithMockUser(roles = ADMIN)
//  public void shouldCreateUser() {
//    //given
//    CreateUserDto createUserDto = getCreateUserDto();
//    User dbUser = userMapper.createUserDtoToEntity(createUserDto);
//    dbUser.setId(1L);
//    UrlToken token = generateUrlToken(null, dbUser, URL_EXPIRATION);
//    String url = getResetPasswordUrl(token.getToken());
//    UrlTokenDto urlTokenDto = generateTokenDto(token);
//
//    //when
//    when(userRepository.findByEmail(dbUser.getEmail())).thenReturn(Optional.empty());
//    when(userRepository.save(any(User.class))).thenReturn(dbUser);
//    when(tokenService.createUrlToken(null, dbUser)).thenReturn(urlTokenDto);
//    when(tokenService.generateEmailUrl(urlTokenDto.getToken(), RESET_PASSWORD_PATH)).thenReturn(url);
//    doNothing().when(emailService).sendSimpleEmail(any(EmailDto.class), any(EmailType.class));
//
//    UserDto actual = userService.createUser(createUserDto);
//
//    //then
//    assertThat(actual).isNotNull();
//    assertThat(actual.getEmail()).isEqualTo(createUserDto.getEmail());
//    assertThat(actual.getName()).isEqualTo(createUserDto.getName());
//    assertThat(actual.getSurname()).isEqualTo(createUserDto.getSurname());
//    assertThat(actual.getId()).isEqualTo(dbUser.getId());
//
//    verify(emailService, times(1)).sendSimpleEmail(emailCaptor.capture(), any(EmailType.class));
//    EmailDto actualEmail = emailCaptor.getValue();
//    assertEquals(createUserDto.getEmail(), actualEmail.getTo());
//    assertEquals(EmailType.CREATE_USER.getSubject(), actualEmail.getSubject());
//    String expectedBody = String
//        .format(EmailType.CREATE_USER.getBody(), createUserDto.getName(), createUserDto.getSurname(), url);
//
//    assertEquals(expectedBody, actualEmail.getBody());
//  }
//
//  @Test
//  @WithMockUser(roles = USER, username = "test@valtech.com")
//  void shouldChangeUserPassword() {
//    //given
//    User user = generateRandomUser(true);
//    String oldPassword = "oldPassword";
//    user.setEmail("test@valtech.com");
//    user.setPassword(oldPassword);
//    ChangeUserPasswordDto changeUserPasswordDto = getChangePasswordDto();
//
//    //when
//    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//    when(passwordEncoder.matches(eq(changeUserPasswordDto.getOldPassword()), eq(user.getPassword())))
//        .thenReturn(true);
//    when(passwordEncoder.encode(eq(changeUserPasswordDto.getNewPassword())))
//        .thenReturn(changeUserPasswordDto.getNewPassword());
//    when(userRepository.save(any(User.class))).thenReturn(user);
//
//    userService.changePassword(changeUserPasswordDto);
//
//    //then
//    assertThat(user.getPassword()).isEqualTo(changeUserPasswordDto.getNewPassword());
//  }
//
//  @Test
//  @WithMockUser(roles = USER, username = "test@valtech.com")
//  void shouldThrowIncorrectPassword() {
//    //given
//    User user = generateRandomUser(true);
//    user.setEmail("test@valtech.com");
//    user.setPassword("password");
//    ChangeUserPasswordDto changeUserPasswordDto = getChangePasswordDto();
//    changeUserPasswordDto.setOldPassword("wrongPassword");
//
//    //when
//    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//    when(passwordEncoder.matches(eq(changeUserPasswordDto.getOldPassword()), eq(user.getPassword())))
//        .thenReturn(false);
//
//    //then
//    Exception exception = assertThrows(IncorrectPassword.class,
//        () -> userService.changePassword(changeUserPasswordDto));
//    String expectedMessage = "Incorrect password";
//    String actualMessage = exception.getMessage();
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  public void shouldFindUserById() {
//    //given
//    User user = generateRandomUser(true);
//
//    //when
//    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//
//    UserDto actual = userService.findById(user.getId());
//
//    //then
//    assertThat(actual).isNotNull();
//    assertThat(actual.getId()).isEqualTo(user.getId());
//    assertThat(actual.getName()).isEqualTo(user.getName());
//    assertThat(actual.getSurname()).isEqualTo(user.getSurname());
//    assertThat(actual.getEmail()).isEqualTo(user.getEmail());
//  }
//
//  @Test
//  @WithMockUser(roles = USER)
//  public void shouldThrowInactiveUserException() {
//    //given
//    User user = generateRandomUser(true);
//    user.setStatus(UserStatus.INACTIVE);
//
//    //when
//    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//
//    //then
//    Exception exception = assertThrows(UserNotFoundException.class,
//        () -> userService.findById(user.getId()));
//    String expectedMessage = String.format("User: %d not found", user.getId());
//    String actualMessage = exception.getMessage();
//    assertEquals(expectedMessage, actualMessage);
//
//  }
//
//  @Test
//  @WithMockUser(roles = USER, username = "test@valtech.com")
//  public void shouldGetMyProfile() {
//    //given
//    User user = generateRandomUser(true);
//    user.setEmail("test@valtech.com");
//
//    //when
//    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//
//    UserDto actual = userService.getMyProfile();
//
//    //then
//    assertThat(actual).isNotNull();
//    assertThat(actual.getId()).isEqualTo(user.getId());
//    assertThat(actual.getName()).isEqualTo(user.getName());
//    assertThat(actual.getSurname()).isEqualTo(user.getSurname());
//    assertThat(actual.getEmail()).isEqualTo(user.getEmail());
//  }
//
//  @Test
//  @WithMockUser(roles = ADMIN, username = "test@valtech.com")
//  public void shouldChangeUserStatus() {
//    //given
//    User user = generateRandomUser(true);
//    user.setStatus(UserStatus.INACTIVE);
//
//    //when
//    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//    when(userRepository.save(any(User.class))).thenReturn(user);
//    UserDto actual = userService.changeUserStatus(user.getId(), UserStatus.ACTIVE);
//
//    //then
//    assertThat(actual).isNotNull();
//    assertThat(actual.getId()).isEqualTo(user.getId());
//    assertThat(actual.getName()).isEqualTo(user.getName());
//    assertThat(actual.getSurname()).isEqualTo(user.getSurname());
//    assertThat(actual.getEmail()).isEqualTo(user.getEmail());
//    assertThat(actual.getStatus()).isEqualTo(UserStatus.ACTIVE);
//  }
//
//  @Test
//  @WithMockUser(roles = ADMIN, username = "test@valtech.com")
//  public void shouldNotChangeTheirUserStatus() {
//    //given
//    User user = generateRandomUser(true);
//    user.setEmail("test@valtech.com");
//
//    //when
//    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//
//    //then
//    Exception exception = assertThrows(ForbiddenException.class,
//        () -> userService.changeUserStatus(user.getId(), UserStatus.INACTIVE));
//    String expectedMessage = String.format("User: %s does not have permission to update this field", user.getEmail());
//    String actualMessage = exception.getMessage();
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  @WithMockUser(roles = USER, username = "test@valtech.com")
//  public void shouldThrowUserNotFoundExceptionWithEmail() {
//    //given
//    String email = "test@valtech.com";
//
//    //when
//    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
//
//    //then
//    Exception exception = assertThrows(UserNotFoundException.class,
//        () -> userService.getMyProfile());
//    String expectedMessage = String.format("User with email: %s not found", email);
//    String actualMessage = exception.getMessage();
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  public void shouldThrowUserNotFoundExceptionWithId() {
//    //given
//    Long id = 12345L;
//
//    //when
//    when(userRepository.findById(id)).thenReturn(Optional.empty());
//
//    //then
//    Exception exception = assertThrows(UserNotFoundException.class,
//        () -> userService.findById(id));
//    String expectedMessage = String.format("User: %d not found", id);
//    String actualMessage = exception.getMessage();
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  public void duplicateEmail() {
//    //given
//    User user = generateRandomUser(true);
//
//    CreateUserDto createUserDto = getCreateUserDto();
//    createUserDto.setEmail(user.getEmail());
//
//    //when
//    Mockito.when(this.userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//
//    //then
//    Throwable exception = assertThrows(DuplicateEmailException.class,
//        () -> this.userService.createUser(createUserDto));
//    Assertions
//        .assertEquals(String.format("Email: %s already exists", createUserDto.getEmail()), exception.getMessage());
//  }
//
//  @Test
//  @WithMockUser(roles = ADMIN)
//  public void shouldFindAllUsersAdminRole() {
//    //given
//    User user = generateRandomUser(true);
//    Pageable pageable = getPageable();
//    //when
//    when(userRepository.findAllByOrderByNameAscSurnameAsc(pageable)).thenReturn(
//        Stream.of(user).collect(Collectors.toList()));
//    List<UserDto> usersList = userService.listAllUsers(0, 2);
//
//    //then
//    assertThat(usersList.size()).isEqualTo(1);
//  }
//
//  @Test
//  @WithMockUser(roles = USER)
//  public void shouldFindAllUsersUserRole() {
//    //given
//    User user1 = generateRandomUser(true);
//    user1.setStatus(UserStatus.ACTIVE);
//    User user2 = generateRandomUser(true);
//    user2.setStatus(UserStatus.INACTIVE);
//    Pageable pageable = getPageable();
//
//    //when
//    when(userRepository.findAllByStatusOrderByNameAscSurnameAsc(UserStatus.ACTIVE, pageable)).thenReturn(
//        Arrays.asList(user1));
//    when(userRepository.findAllByOrderByNameAscSurnameAsc(pageable)).thenReturn(Arrays.asList(user1, user2));
//
//    List<UserDto> usersList = userService.listAllUsers(0, 2);
//
//    //then
//    assertThat(usersList.size()).isEqualTo(1);
//  }
//
//  @Test
//  public void shouldMapUserToDto() {
//    //given
//    User user = generateRandomUser(true);
//
//    //when
//    UserDto result = userMapper.toDto(user);
//
//    //then
//    assertThat(result).isNotNull();
//    assertThat(result.getId()).isEqualTo(user.getId());
//    assertThat(result.getName()).isEqualTo(user.getName());
//    assertThat(result.getSurname()).isEqualTo(user.getSurname());
//    assertThat(result.getEmail()).isEqualTo(user.getEmail());
//  }
//
//  @Test
//  public void shouldMapUsersListToDto() {
//    //given
//    List<User> usersList = getUsersList();
//
//    //when
//    List<UserDto> userDtoList = userMapper.listToDto(usersList);
//
//    //then
//    assertThat(usersList).isEqualTo(userDtoList);
//  }
//
//  @Test
//  @WithMockUser(roles = {USER, ADMIN}, username = "test@valtech.com")
//  public void shouldUpdateTheirProfile() {
//    //given
//    String email = "test@valtech.com";
//    User user = generateRandomUser(true);
//    user.setEmail(email);
//
//    UpdateUserDto updateUserDto = getUpdateUserDto(email);
//
//    User expectedInDb = User.builder()
//        .id(user.getId())
//        .name(updateUserDto.getName())
//        .surname(updateUserDto.getSurname())
//        .email(email)
//        .userRole(Role.USER)
//        .status(UserStatus.ACTIVE)
//        .build();
//
//    //when
//    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//    when(userRepository.save(eq(expectedInDb))).thenReturn(expectedInDb);
//
//    UserDto actual = userService.updateUser(user.getId(), updateUserDto);
//
//    //then
//    assertThat(actual).isNotNull();
//    assertThat(actual.getEmail()).isEqualTo(updateUserDto.getEmail());
//    assertThat(actual.getName()).isEqualTo(updateUserDto.getName());
//    assertThat(actual.getSurname()).isEqualTo(updateUserDto.getSurname());
//    assertThat(user.getId()).isEqualTo(actual.getId());
//  }
//
//  @Test
//  @WithMockUser(roles = USER, username = "test@valtech.com")
//  public void shouldNotUpdateOthersProfile() {
//    //given
//    User user = generateRandomUser(true);
//    UpdateUserDto updateUserDto = getUpdateUserDto(user.getEmail());
//
//    //when
//    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//
//    //then
//    Exception exception = assertThrows(ForbiddenException.class,
//        () -> userService.updateUser(user.getId(), updateUserDto));
//    String expectedMessage = String
//        .format("User: %s does not have permission to update user with id: %d", "test@valtech.com", user.getId());
//    String actualMessage = exception.getMessage();
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  @WithMockUser(roles = USER, username = "test@valtech.com")
//  public void shouldNotUpdateTheirEmail() {
//    //given
//    User user = generateRandomUser(true);
//    user.setEmail("test@valtech.com");
//    String email = UUID.randomUUID() + "@valtech.com";
//    UpdateUserDto updateUserDto = getUpdateUserDto(email);
//
//    //when
//    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//
//    //then
//    Exception exception = assertThrows(ForbiddenException.class,
//        () -> userService.updateUser(user.getId(), updateUserDto));
//    String expectedMessage = String
//        .format("User: %s does not have permission to update this field", "test@valtech.com");
//    String actualMessage = exception.getMessage();
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @ParameterizedTest
//  @MethodSource("passwordStrength")
//  public void passwordComplexityTest(String password, Integer expectedScore) {
//    //given
//    PasswordDto passwordDto = getResetPasswordDto();
//    passwordDto.setPassword(password);
//
//    //when
//    Integer actualScore = userService.checkPasswordComplexity(passwordDto);
//
//    //then
//    assertThat(expectedScore).isEqualTo(actualScore);
//  }
//
//  @ParameterizedTest
//  @MethodSource("invalidPasswords")
//  public void passwordComplexityExceptionTest(String password) {
//    //given
//    PasswordDto passwordDto = getResetPasswordDto();
//    passwordDto.setPassword(password);
//    //when
//    Exception exception = assertThrows(InvalidPasswordException.class,
//        () -> userService.checkPasswordComplexity(passwordDto));
//
//    //then
//    String expectedMessage = "Password length must be at least 8 characters and password strength must be at least 2";
//    String actualMessage = exception.getMessage();
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  public void shouldFindAllRoles() {
//    //given
//    List<Role> rolesList = Arrays.stream(Role.values()).collect(Collectors.toList());
//
//    //when
//    List<Role> result = userService.listAllRoles();
//
//    //then
//    assertThat(rolesList).isEqualTo(result);
//  }
//
//  @Test
//  public void shouldRequestResetPassword() {
//    //given
//    String email = "test@valtech.com";
//    User user = generateRandomUser(false);
//    UrlToken token = generateUrlToken(null, user, URL_EXPIRATION);
//    String url = getResetPasswordUrl(token.getToken());
//    UrlTokenDto urlTokenDto = generateTokenDto(token);
//    //when
//    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
//    when(tokenService.createUrlToken(null, user)).thenReturn(urlTokenDto);
//    when(tokenService.generateEmailUrl(token.getToken(), RESET_PASSWORD_PATH)).thenReturn(url);
//    doNothing().when(emailService).sendSimpleEmail(any(EmailDto.class), any(EmailType.class));
//    userService.requestResetPassword(email);
//
//    //then
//    verify(emailService, times(1)).sendSimpleEmail(emailCaptor.capture(), any(EmailType.class));
//    EmailDto actual = emailCaptor.getValue();
//    assertEquals(EmailType.RESET_PASSWORD.getSubject(), actual.getSubject());
//    String expectedBody = String.format(EmailType.RESET_PASSWORD.getBody(), user.getName(), user.getSurname(), url);
//    assertEquals(expectedBody, actual.getBody());
//    assertEquals(email, actual.getTo());
//  }
//
//  @Test
//  public void shouldResetPassword() {
//    //given
//    User user = generateRandomUser(true);
//    user.setEmail("test@valtech.com");
//    PasswordDto passwordDto = getResetPasswordDto();
//    UrlToken token = generateUrlToken(null, user, URL_EXPIRATION);
//
//    //when
//    when(tokenService.findByToken(token.getToken())).thenReturn(token);
//    when(passwordEncoder.encode(eq(passwordDto.getPassword())))
//        .thenReturn(passwordDto.getPassword());
//    when(userRepository.save(any(User.class))).thenReturn(user);
//
//    userService.resetPassword(passwordDto, token.getToken());
//
//    //then
//    assertThat(user.getPassword()).isEqualTo(passwordDto.getPassword());
//  }
//
//  @ParameterizedTest
//  @MethodSource("invalidInput")
//  public void shouldThrowInvalidInputExceptionWhenCreatingUser(String name, String lastName) {
//    //given
//    CreateUserDto createUserDto = getCreateUserDto();
//    createUserDto.setName(name);
//    createUserDto.setSurname(lastName);
//
//    //when
//    when(userService.findByEmail(createUserDto.getEmail())).thenReturn(Optional.empty());
//
//    //then
//    Assertions.assertThrows(InvalidInputException.class, () -> userService.createUser(createUserDto));
//  }
//
//  @ParameterizedTest
//  @MethodSource("invalidInput")
//  public void shouldThrowInvalidInputExceptionWhenUpdateUser(String name, String lastName) {
//    //given
//    User user = generateRandomUser(true);
//    UpdateUserDto updateUserDto = getUpdateUserDto(user.getEmail());
//    updateUserDto.setName(name);
//    updateUserDto.setSurname(lastName);
//
//    //when
//    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//
//    //then
//    Assertions.assertThrows(InvalidInputException.class, () -> userService.updateUser(user.getId(), updateUserDto));
//
//  }
//
//  @Test
//  @WithMockUser(roles = USER, username = "test@valtech.com")
//  public void shouldUploadImage() {
//    //given
//    User user = generateRandomUser(true);
//    user.setEmail("test@valtech.com");
//    MockMultipartFile file = generateMultiPartFile();
//
//    //when & then
//    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//
//    userService.uploadImage(user.getId(), file);
//  }
//
//  @Test
//  @WithMockUser(roles = USER, username = "test@valtech.com")
//  public void shouldNotUploadOthersImage() {
//    //given
//    User user = generateRandomUser(true);
//    MockMultipartFile file = generateMultiPartFile();
//
//    //when
//    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//
//    //then
//    Assertions.assertThrows(ForbiddenException.class, () -> userService.uploadImage(user.getId(), file));
//  }
//
//  @Test
//  @WithMockUser(roles = USER, username = "test@valtech.com")
//  public void shouldNotUploadInvalidImageType() {
//    //given
//    User user = generateRandomUser(true);
//    MockMultipartFile file = generateInvalidMultiPartFile();
//    //when
//    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//
//    //then
//    Assertions.assertThrows(ImageException.class, () -> userService.uploadImage(user.getId(), file));
//  }
//
//  private List<User> getUsersList() {
//    return this.userRepository.findAll();
//  }
//
//}