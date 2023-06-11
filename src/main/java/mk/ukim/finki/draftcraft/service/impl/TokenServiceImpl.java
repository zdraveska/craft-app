package mk.ukim.finki.draftcraft.service.impl;

import mk.ukim.finki.draftcraft.domain.common.UrlToken;
import mk.ukim.finki.draftcraft.domain.exceptions.TokenIsNotValidException;
import mk.ukim.finki.draftcraft.domain.exceptions.TokenNotFoundException;
import mk.ukim.finki.draftcraft.domain.users.AccountRequest;
import mk.ukim.finki.draftcraft.domain.users.User;
import mk.ukim.finki.draftcraft.dto.UrlTokenDto;
import mk.ukim.finki.draftcraft.mapper.TokenMapper;
import mk.ukim.finki.draftcraft.repository.TokenRepository;
import mk.ukim.finki.draftcraft.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

  private final TokenRepository tokenRepository;
  private final int expiration;
  private final String baseUrl;
  private final TokenMapper tokenMapper;

  public TokenServiceImpl(TokenRepository tokenRepository,
      @Value("${application.url.baseUrl}") String baseUrl,
      @Value("${application.url.token.expiration}") int expiration,
      TokenMapper tokenMapper) {
    this.tokenRepository = tokenRepository;
    this.expiration = expiration;
    this.baseUrl = baseUrl;
    this.tokenMapper = tokenMapper;
  }

  @Override
  public UrlTokenDto createUrlToken(AccountRequest accountRequest, User user) {
    String tokenValue = UUID.randomUUID().toString();
    UrlToken token = tokenMapper.toEntity(tokenValue, user, expiration, accountRequest);

    token = tokenRepository.save(token);
    return tokenMapper.toDto(token);
  }

  @Override
  public String generateEmailUrl(String token, String pathUrl) {
    return baseUrl + pathUrl + token;
  }

  @Override
  public UrlToken findByToken(String tokenValue) {
    UrlToken token = tokenRepository.findByToken(tokenValue).orElseThrow(TokenNotFoundException::new);
    if (!token.isValid()) {
      throw new TokenIsNotValidException();
    }
    return token;
  }
}
