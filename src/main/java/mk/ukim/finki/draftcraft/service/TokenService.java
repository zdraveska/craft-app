package mk.ukim.finki.draftcraft.service;


import mk.ukim.finki.draftcraft.domain.model.common.UrlToken;
import mk.ukim.finki.draftcraft.domain.model.user.AccountRequest;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.dto.UrlTokenDto;

public interface TokenService {

  UrlTokenDto createUrlToken(AccountRequest accountRequest, User user);

  String generateEmailUrl(String token, String pathUrl);

  UrlToken findByToken(String tokenValue);

}
