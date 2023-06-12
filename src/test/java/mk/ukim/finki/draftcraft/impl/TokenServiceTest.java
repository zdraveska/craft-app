package mk.ukim.finki.draftcraft.impl;

import mk.ukim.finki.draftcraft.domain.model.common.UrlToken;
import mk.ukim.finki.draftcraft.domain.exceptions.TokenIsNotValidException;
import mk.ukim.finki.draftcraft.domain.exceptions.TokenNotFoundException;
import mk.ukim.finki.draftcraft.domain.model.user.AccountRequest;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.dto.UrlTokenDto;
import mk.ukim.finki.draftcraft.mapper.TokenMapper;
import mk.ukim.finki.draftcraft.repository.TokenRepository;
import mk.ukim.finki.draftcraft.service.TokenService;
import mk.ukim.finki.draftcraft.service.impl.TokenServiceImpl;
import mk.ukim.finki.draftcraft.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest extends BaseServiceTest {

  TokenService tokenService;

  @Mock
  TokenRepository tokenRepository;

  TokenMapper tokenMapper = Mappers.getMapper(TokenMapper.class);

  @BeforeEach
  public void setUp() {
    tokenService = new TokenServiceImpl(tokenRepository, BASE_URL, URL_EXPIRATION, tokenMapper);
  }

  @Test
  public void shouldFindToken() {
    //given
    UrlToken token = generateUrlToken(null, null, URL_EXPIRATION);

    //when
    when(tokenRepository.findByToken(token.getToken())).thenReturn(Optional.of(token));

    UrlToken actual = tokenService.findByToken(token.getToken());

    //then
    assertThat(actual).isNotNull();
    assertThat(actual.getToken()).isEqualTo(token.getToken());
    assertThat(actual.getExpiration()).isEqualTo(token.getExpiration());
    assertThat(actual.getUser()).isEqualTo(token.getUser());
    assertThat(actual.getAccountRequest()).isEqualTo(token.getAccountRequest());
  }

  @Test
  public void shouldThrowTokenNotFoundException() {
    //given
    String tokenValue = UUID.randomUUID().toString();

    //when
    when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.empty());

    //then
    Exception exception = assertThrows(TokenNotFoundException.class,
        () -> tokenService.findByToken(tokenValue));

    String expectedMessage = "Token not found";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));

  }

  @Test
  public void shouldCreateUrlToken() {
    //given
    AccountRequest accountRequest = generateRandomAccountRequest(true, false);
    User user = generateRandomUser(true);
    UrlToken token = generateUrlToken(accountRequest, user, URL_EXPIRATION);

    //when
    when(tokenRepository.save(any(UrlToken.class))).thenReturn(token);

    UrlTokenDto actual = tokenService.createUrlToken(accountRequest, user);

    //then
    assertThat(actual).isNotNull();
    assertThat(actual.getToken()).isEqualTo(token.getToken());
    assertThat(actual.getExpiration()).isEqualTo(token.getExpiration());
    assertThat(actual.getUser()).isEqualTo(token.getUser());
    assertThat(actual.getAccountRequest()).isEqualTo(token.getAccountRequest());

  }

  @Test
  public void shouldThrowTokenIsNotValidException() {
    //given
    User user = generateRandomUser(true);
    user.setEmail(USER_EMAIL);

    UrlToken token = generateUrlToken(null, user, -1);

    //when
    when(tokenRepository.findByToken(token.getToken())).thenReturn(Optional.of(token));

    //then
    Exception exception = assertThrows(TokenIsNotValidException.class,
        () -> tokenService.findByToken(token.getToken()));
    assertEquals("Token not valid.", exception.getMessage());
  }

}
