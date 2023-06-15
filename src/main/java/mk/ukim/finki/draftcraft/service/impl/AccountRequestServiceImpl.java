package mk.ukim.finki.draftcraft.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.draftcraft.domain.enumeration.EmailType;
import mk.ukim.finki.draftcraft.domain.model.common.UrlToken;
import mk.ukim.finki.draftcraft.domain.exceptions.AccountRequestAlreadyHandledException;
import mk.ukim.finki.draftcraft.domain.exceptions.AccountRequestEmailIsAlreadyConfirmedException;
import mk.ukim.finki.draftcraft.domain.exceptions.AccountRequestNotFoundException;
import mk.ukim.finki.draftcraft.domain.exceptions.DuplicateEmailException;
import mk.ukim.finki.draftcraft.domain.model.user.AccountRequest;
import mk.ukim.finki.draftcraft.domain.model.user.AccountRequestStatus;
import mk.ukim.finki.draftcraft.dto.AccountRequestDto;
import mk.ukim.finki.draftcraft.dto.EmailDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateAccountRequestDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateUserDto;
import mk.ukim.finki.draftcraft.mapper.AccountRequestMapper;
import mk.ukim.finki.draftcraft.repository.AccountRequestRepository;
import mk.ukim.finki.draftcraft.service.AccountRequestService;
import mk.ukim.finki.draftcraft.service.EmailService;
import mk.ukim.finki.draftcraft.service.TokenService;
import mk.ukim.finki.draftcraft.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static mk.ukim.finki.draftcraft.util.EmailUtil.generateEmail;
import static mk.ukim.finki.draftcraft.util.ValidationUtil.checkIfAlphabetic;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountRequestServiceImpl implements AccountRequestService {

  private final AccountRequestRepository accountRequestRepository;
  private final AccountRequestMapper accountRequestMapper;
  private final EmailService emailService;
  private final TokenService tokenService;
  private final UserService userService;

  static final String CONFIRM_EMAIL_PATH = "/api/account-requests/confirm-email?token=";

  @CacheEvict(cacheNames = {"account-requests"}, allEntries = true)
  @Override
  public AccountRequestDto createAccountRequest(CreateAccountRequestDto createAccountRequestDto) {
    if (userService.findByEmail(createAccountRequestDto.getEmail()).isPresent()) {
      throw new DuplicateEmailException(createAccountRequestDto.getEmail());
    }

    checkIfAlphabetic(createAccountRequestDto.getName(), createAccountRequestDto.getSurname());

    LocalDate currentDate = LocalDate.now();
    AccountRequest accountRequest = accountRequestMapper.toEntity(createAccountRequestDto, currentDate);
    accountRequest = accountRequestRepository.save(accountRequest);

    String tokenValue = tokenService.createUrlToken(accountRequest, null).getToken();
    String url = tokenService.generateEmailUrl(tokenValue, CONFIRM_EMAIL_PATH);

    EmailDto emailDto = generateEmail(createAccountRequestDto.getEmail(),
            createAccountRequestDto.getName(),
            createAccountRequestDto.getSurname(),
            EmailType.CONFIRM_EMAIL_ADDRESS,
            url);
    log.info("Account request {} created", accountRequest);

    emailService.sendSimpleEmail(emailDto, EmailType.CONFIRM_EMAIL_ADDRESS);

    return accountRequestMapper.toDto(accountRequest);
  }

  @Cacheable(cacheNames = {"account-requests"})
  @Override
  public List<AccountRequestDto> findAll() {
    List<AccountRequest> accountRequests = accountRequestRepository.findAll();
    return accountRequestMapper.listToDto(accountRequests);
  }

  @CacheEvict(cacheNames = {"account-requests"}, key = "#id", allEntries = true)
  @Override
  public AccountRequestDto acceptAccountRequest(Long id, AccountRequestStatus status) {
    AccountRequest accountRequest = accountRequestRepository.findById(id)
        .orElseThrow(() -> new AccountRequestNotFoundException(id));

    if (!AccountRequestStatus.PENDING.equals(accountRequest.getStatus())) {
      throw new AccountRequestAlreadyHandledException(accountRequest.getId(), accountRequest.getStatus());
    }

    accountRequest.setStatus(status);
    accountRequestRepository.save(accountRequest);
    if (AccountRequestStatus.APPROVED.equals(status)) {
      CreateUserDto createUserDto = accountRequestMapper.toCreateUserDto(accountRequest);
      userService.createUser(createUserDto);
      log.info("New user has been successfully added");
    } else {
      log.info("The request was denied");
    }

    return accountRequestMapper.toDto(accountRequest);
  }

  @CacheEvict(cacheNames = {"account-requests"}, allEntries = true)
  @Override
  public void confirmEmail(String tokenValue) {
    UrlToken token = tokenService.findByToken(tokenValue);

    AccountRequest accountRequest = token.getAccountRequest();
    if (accountRequest.getEmailConfirmed()) {
      throw new AccountRequestEmailIsAlreadyConfirmedException();
    }
    accountRequest.setEmailConfirmed(true);

    accountRequestRepository.save(accountRequest);
    log.info("Confirm email");

  }

}