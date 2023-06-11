//package mk.ukim.finki.draftcraft.impl;
//
//
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
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AccountRequestServiceTest extends BaseServiceTest {
//
//  @Mock
//  AccountRequestRepository accountRequestRepository;
//
//  AccountRequestMapper accountRequestMapper = Mappers.getMapper(AccountRequestMapper.class);
//
//  AccountRequestService accountRequestService;
//
//  @Mock
//  UserService userService;
//
//  @Mock
//  EmailService emailService;
//
//  @Mock
//  TokenRepository tokenRepository;
//
//  @Captor
//  ArgumentCaptor<EmailDto> emailCaptor;
//
//  private static final String CONFIRM_EMAIL_PATH = "/confirm-email?token=";
//
//  @BeforeEach
//  public void setUp() {
//    accountRequestService = new AccountRequestServiceImpl(accountRequestRepository, accountRequestMapper,
//        emailService, tokenService, userService);
//  }
//
//  @Test
//  public void shouldCreateAccountRequest() {
//    //given
//    CreateAccountRequestDto createAccountRequestDto = getAccountRequestDto();
//    AccountRequest accountRequest = accountRequestMapper.toEntity(createAccountRequestDto, LocalDate.now());
//    accountRequest.setId(1L);
//
//    UrlToken token = generateUrlToken(accountRequest, null, URL_EXPIRATION);
//    UrlTokenDto urlTokenDto = generateTokenDto(token);
//
//    String url = getConfirmEmailUrl(BASE_URL, CONFIRM_EMAIL_PATH, urlTokenDto.getToken());
//
//    //when
//    when(userService.findByEmail(createAccountRequestDto.getEmail())).thenReturn(Optional.empty());
//    when(accountRequestRepository.save(any(AccountRequest.class))).thenReturn(accountRequest);
//    when(tokenService.createUrlToken(accountRequest, null)).thenReturn(urlTokenDto);
//    doNothing().when(emailService).sendSimpleEmail(any(EmailDto.class), any(EmailType.class));
//    when(tokenService.generateEmailUrl(token.getToken(), CONFIRM_EMAIL_PATH)).thenReturn(url);
//    AccountRequestDto actual = accountRequestService.createAccountRequest(createAccountRequestDto);
//
//    //then
//    assertThat(actual).isNotNull();
//    assertThat(actual.getEmail()).isEqualTo(createAccountRequestDto.getEmail());
//    assertThat(actual.getName()).isEqualTo(createAccountRequestDto.getName());
//    assertThat(actual.getSurname()).isEqualTo(createAccountRequestDto.getSurname());
//    assertThat(actual.getId()).isNotNull();
//    assertThat(actual.getId()).isEqualTo(accountRequest.getId());
//    assertThat(actual.getStatus()).isEqualTo(AccountRequestStatus.PENDING);
//    assertThat(actual.getCreatedDate()).isEqualTo(LocalDate.now());
//  }
//
//  @Test
//  public void shouldThrowDuplicateEmailException() {
//    //given
//    CreateAccountRequestDto createAccountRequestDto = getAccountRequestDto();
//    User user = User.builder()
//        .name(createAccountRequestDto.getName())
//        .surname(createAccountRequestDto.getSurname())
//        .email(createAccountRequestDto.getEmail()).build();
//
//    //when
//    when(userService.findByEmail(createAccountRequestDto.getEmail())).thenReturn(Optional.of(user));
//
//    //then
//    Exception exception = assertThrows(DuplicateEmailException.class,
//        () -> accountRequestService.createAccountRequest(createAccountRequestDto));
//    String exceptedMessage = String.format("Email: %s already exists", createAccountRequestDto.getEmail());
//    assertEquals(exceptedMessage, exception.getMessage());
//  }
//
//  @Test
//  public void shouldFindAllAccountRequests() {
//    //given
//    AccountRequest accountRequest = generateRandomAccountRequest(true, true);
//    accountRequest.setEmailConfirmed(true);
//    Pageable pageable = getPageable();
//    //when
//    when(accountRequestRepository.findAllByEmailConfirmedIsTrueOrderByNameAscSurnameAsc(pageable)).thenReturn(
//        Stream.of(accountRequest).collect(Collectors.toList()));
//    List<AccountRequestDto> accountRequestList = accountRequestService.findAll(null, 0, 2);
//
//    //then
//    assertThat(accountRequestList.size()).isEqualTo(1);
//  }
//
//  @Test
//  public void shouldFindAllPendingAccountRequests() {
//    //given
//    AccountRequest accountRequest = generateRandomAccountRequest(true, true);
//    accountRequest.setEmailConfirmed(true);
//    Pageable pageable = getPageable();
//    AccountRequestStatus status = AccountRequestStatus.PENDING;
//
//    //when
//    when(accountRequestRepository
//        .findAllByStatusAndEmailConfirmedIsTrueOrderByNameAscSurnameAsc(status, pageable))
//        .thenReturn(
//            Stream.of(accountRequest).collect(Collectors.toList()));
//    List<AccountRequestDto> accountRequestList = accountRequestService.findAll(status, 0, 2);
//
//    //then
//    assertThat(accountRequestList.size()).isEqualTo(1);
//  }
//
//  @Test
//  public void confirmEmailTest() {
//
//    //given
//    AccountRequest accountRequest = generateRandomAccountRequest(true, false);
//    UrlToken token = generateUrlToken(accountRequest, null, URL_EXPIRATION);
//    tokenRepository.save(token);
//    String tokenValue = token.getToken();
//
//    //when
//    when(tokenService.findByToken(tokenValue)).thenReturn(token);
//    accountRequestService.confirmEmail(tokenValue);
//
//    //then
//    assertThat(accountRequest.getEmailConfirmed()).isEqualTo(true);
//
//  }
//
//  @Test
//  public void shouldMapEntityToDto() {
//    //given
//    AccountRequest accountRequest = generateRandomAccountRequest(true, true);
//
//    //when
//    AccountRequestDto actual = accountRequestMapper.toDto(accountRequest);
//
//    //then
//    assertThat(actual).isNotNull();
//    assertThat(actual.getId()).isEqualTo(accountRequest.getId());
//    assertThat(actual.getName()).isEqualTo(accountRequest.getName());
//    assertThat(actual.getSurname()).isEqualTo(accountRequest.getSurname());
//    assertThat(actual.getEmail()).isEqualTo(accountRequest.getEmail());
//    assertThat(actual.getCreatedDate()).isEqualTo(accountRequest.getCreatedDate());
//
//  }
//
//  @Test
//  public void shouldMapListToDto() {
//    //given
//    List<AccountRequest> accountRequestList = getAccountRequestsList();
//
//    //when
//    List<AccountRequestDto> accountRequestDtoList = accountRequestMapper.listToDto(accountRequestList);
//
//    //then
//    assertThat(accountRequestList).isEqualTo(accountRequestDtoList);
//  }
//
//  @Test
//  public void shouldApproveAccountRequest() {
//    //given
//    AccountRequest accountRequest = generateRandomAccountRequest(true, true);
//    CreateUserDto createUserDto = accountRequestMapper.toCreateUserDto(accountRequest);
//    UserDto userDto = UserDto.builder()
//        .name(createUserDto.getName())
//        .surname(createUserDto.getSurname())
//        .email(createUserDto.getEmail()).build();
//
//    //when
//    when(accountRequestRepository.findById(accountRequest.getId())).thenReturn(Optional.of(accountRequest));
//    when(userService.createUser(createUserDto)).thenReturn(userDto);
//    when(accountRequestRepository.save(any(AccountRequest.class))).thenReturn(accountRequest);
//    AccountRequestDto actual = accountRequestService
//        .handleAccountRequest(accountRequest.getId(), AccountRequestStatus.APPROVED);
//
//    //then
//    assertThat(actual).isNotNull();
//    assertEquals(accountRequest.getEmail(), actual.getEmail());
//    assertEquals(accountRequest.getName(), actual.getName());
//    assertEquals(accountRequest.getSurname(), actual.getSurname());
//    assertEquals(accountRequest.getId(), actual.getId());
//    assertEquals(accountRequest.getStatus(), actual.getStatus());
//    assertEquals(AccountRequestStatus.APPROVED, actual.getStatus());
//    assertEquals(accountRequest.getCreatedDate(), actual.getCreatedDate());
//  }
//
//  @Test
//  public void shouldDeclineAccountRequest() {
//    //given
//    AccountRequest accountRequest = generateRandomAccountRequest(true, true);
//
//    //when
//    when(accountRequestRepository.findById(accountRequest.getId())).thenReturn(Optional.of(accountRequest));
//    when(accountRequestRepository.save(any(AccountRequest.class))).thenReturn(accountRequest);
//    doNothing().when(emailService).sendSimpleEmail(any(EmailDto.class), any(EmailType.class));
//
//    AccountRequestDto actual = accountRequestService
//        .handleAccountRequest(accountRequest.getId(), AccountRequestStatus.DECLINED);
//
//    //then
//    assertThat(actual).isNotNull();
//    assertEquals(accountRequest.getEmail(), actual.getEmail());
//    assertEquals(accountRequest.getName(), actual.getName());
//    assertEquals(accountRequest.getSurname(), actual.getSurname());
//    assertEquals(accountRequest.getId(), actual.getId());
//    assertEquals(accountRequest.getStatus(), actual.getStatus());
//    assertEquals(AccountRequestStatus.DECLINED, actual.getStatus());
//    assertEquals(accountRequest.getCreatedDate(), actual.getCreatedDate());
//
//    verify(emailService).sendSimpleEmail(emailCaptor.capture(), any(EmailType.class));
//    EmailDto actualEmail = emailCaptor.getValue();
//    assertEquals(EmailType.ACCOUNT_REQUEST_DECLINED.getSubject(), actualEmail.getSubject());
//    assertEquals(accountRequest.getEmail(), actualEmail.getTo());
//    String expectedBody = String
//        .format(EmailType.ACCOUNT_REQUEST_DECLINED.getBody(), accountRequest.getName(), accountRequest.getSurname());
//    assertEquals(expectedBody, actualEmail.getBody());
//  }
//
//  @Test
//  public void shouldThrowAccountRequestNotFound() {
//    //given
//    AccountRequest accountRequest = generateRandomAccountRequest(true, false);
//
//    //when
//    when(accountRequestRepository.findById(accountRequest.getId())).thenReturn(Optional.empty());
//
//    //then
//    Exception exception = assertThrows(AccountRequestNotFoundException.class,
//        () -> accountRequestService.handleAccountRequest(accountRequest.getId(), AccountRequestStatus.APPROVED));
//    String expectedMessage = String.format("Account request with id: %d not found.", accountRequest.getId());
//    String actualMessage = exception.getMessage();
//    assertEquals(expectedMessage, actualMessage);
//  }
//
//  @Test
//  public void shouldThrowAccountRequestAlreadyHandledException() {
//    //given
//    AccountRequest accountRequest = generateRandomAccountRequest(true, true);
//    accountRequest.setStatus(AccountRequestStatus.APPROVED);
//
//    //when
//    when(accountRequestRepository.findById(accountRequest.getId())).thenReturn(Optional.of(accountRequest));
//
//    //then
//    Exception exception = assertThrows(AccountRequestAlreadyHandledException.class,
//        () -> accountRequestService.handleAccountRequest(accountRequest.getId(), AccountRequestStatus.DECLINED));
//    String expectedMessage = String.format("Account request: %d has already been %s", accountRequest.getId(),
//        AccountRequestStatus.APPROVED);
//    String actualMessage = exception.getMessage();
//    assertEquals(expectedMessage, actualMessage);
//  }
//
//  @ParameterizedTest
//  @MethodSource("invalidInput")
//  public void shouldThrowInvalidInputException(String name, String surname) {
//    //given
//    CreateAccountRequestDto createAccountRequestDto = getAccountRequestDto();
//    createAccountRequestDto.setName(name);
//    createAccountRequestDto.setSurname(surname);
//
//    //when
//    when(userService.findByEmail(createAccountRequestDto.getEmail())).thenReturn(Optional.empty());
//
//    //then
//    Assertions.assertThrows(InvalidInputException.class,
//        () -> accountRequestService.createAccountRequest(createAccountRequestDto));
//  }
//
//  private List<AccountRequest> getAccountRequestsList() {
//    return this.accountRequestRepository.findAll();
//  }
//
//}