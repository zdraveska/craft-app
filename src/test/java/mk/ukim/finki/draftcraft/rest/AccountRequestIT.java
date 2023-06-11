//package mk.ukim.finki.draftcraft.rest;
//
//import mk.ukim.finki.draftcraft.domain.AccountRequest;
//import mk.ukim.finki.draftcraft.domain.UrlToken;
//import mk.ukim.finki.draftcraft.domain.User;
//import mk.ukim.finki.draftcraft.domain.enumerations.AccountRequestStatus;
//import mk.ukim.finki.draftcraft.dto.input.user.CreateAccountRequestDto;
//import mk.ukim.finki.draftcraft.repository.AccountRequestRepository;
//import mk.ukim.finki.draftcraft.repository.TokenRepository;
//import mk.ukim.finki.draftcraft.repository.UserRepository;
//import mk.ukim.finki.draftcraft.utils.BaseIntegrationTest;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasSize;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class AccountRequestIT extends BaseIntegrationTest {
//
//  @Autowired
//  private AccountRequestRepository accountRequestRepository;
//
//  @Autowired
//  private TokenRepository tokenRepository;
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @Test
//  void createAccountRequest() throws Exception {
//    //given
//    int sizeBefore = accountRequestRepository.findAll().size();
//
//    CreateAccountRequestDto createAccountRequestDto = getAccountRequestDto();
//
//    //when
//    mockMvc.perform(post("/api/account-requests")
//            .contentType("application/json")
//            .content(objectMapper.writeValueAsString(createAccountRequestDto)))
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andExpect(jsonPath("$").isNotEmpty())
//        .andExpect(jsonPath("$.id").isNumber())
//        .andExpect(jsonPath("$.name").value(createAccountRequestDto.getName()))
//        .andExpect(jsonPath("$.surname").value(createAccountRequestDto.getSurname()))
//        .andExpect(jsonPath("$.email").value(createAccountRequestDto.getEmail()))
//        .andExpect(jsonPath("$.status").value(AccountRequestStatus.PENDING.toString()))
//        .andExpect(jsonPath("$.createdDate").value(LocalDate.now().toString()));
//
//    //then
//    List<AccountRequest> accountRequestList = accountRequestRepository.findAll();
//    assertThat(accountRequestList).isNotEmpty();
//    assertThat(accountRequestList).hasSize(sizeBefore + 1);
//
//    Optional<AccountRequest> accountRequest = accountRequestRepository.findByEmail(createAccountRequestDto.getEmail());
//    assertThat(accountRequest).isPresent();
//    assertEquals(createAccountRequestDto.getEmail(), accountRequest.get().getEmail());
//    assertEquals(createAccountRequestDto.getName(), accountRequest.get().getName());
//    assertEquals(createAccountRequestDto.getSurname(), accountRequest.get().getSurname());
//  }
//
//  @Test
//  @WithMockUser(roles = {ADMIN})
//  void listAccountRequests() throws Exception {
//    //given
//    AccountRequest accountRequest1 = generateRandomAccountRequest(false, true);
//    accountRequestRepository.save(accountRequest1);
//    AccountRequest accountRequest2 = generateRandomAccountRequest(false, true);
//    accountRequestRepository.save(accountRequest2);
//    AccountRequest accountRequest3 = generateRandomAccountRequest(false, true);
//    accountRequestRepository.save(accountRequest3);
//    Pageable pageable = getPageable();
//    int size = accountRequestRepository.findAllByEmailConfirmedIsTrueOrderByNameAscSurnameAsc(pageable).size();
//
//    //when
//    mockMvc.perform(get("/api/account-requests")
//            .contentType("application/json")
//            .param("size", String.valueOf(2)))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$", hasSize(size)));
//  }
//
//  @Test
//  @WithMockUser(roles = {ADMIN})
//  void approveAccountRequest() throws Exception {
//    //given
//    AccountRequest accountRequest = generateRandomAccountRequest(false, true);
//    accountRequestRepository.save(accountRequest);
//    AccountRequestStatus status = AccountRequestStatus.APPROVED;
//
//    //when
//    mockMvc.perform(put("/api/account-requests/{id}", accountRequest.getId())
//            .contentType("application/json")
//            .param("status", status.toString()))
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andExpect(jsonPath("$").isNotEmpty())
//        .andExpect(jsonPath("$.id").value(accountRequest.getId()))
//        .andExpect(jsonPath("$.status").value(status.toString()))
//        .andExpect(jsonPath("$.name").value(accountRequest.getName()))
//        .andExpect(jsonPath("$.surname").value(accountRequest.getSurname()))
//        .andExpect(jsonPath("$.email").value(accountRequest.getEmail()))
//        .andExpect(jsonPath("$.createdDate").value(accountRequest.getCreatedDate().toString()));
//
//    //then
//    Optional<User> user = userRepository.findByEmail(accountRequest.getEmail());
//    assertThat(user).isPresent();
//    assertEquals(accountRequest.getEmail(), user.get().getEmail());
//    assertEquals(accountRequest.getName(), user.get().getName());
//    assertEquals(accountRequest.getSurname(), user.get().getSurname());
//  }
//
//  @Test
//  void confirmEmailTest() throws Exception {
//    AccountRequest accountRequest = generateRandomAccountRequest(true, false);
//    accountRequestRepository.save(accountRequest);
//    UrlToken token = generateUrlToken(accountRequest, null, URL_EXPIRATION);
//    String tokenValue = token.getToken();
//    tokenRepository.save(token);
//
//    mockMvc.perform(put("/api/account-requests/confirm-email")
//            .param("token", tokenValue))
//        .andExpect(status().isOk());
//
//    AccountRequest confirmedAccountRequest = accountRequestRepository.findById(accountRequest.getId()).get();
//    assertThat(confirmedAccountRequest.getEmailConfirmed()).isEqualTo(true);
//  }
//
//}