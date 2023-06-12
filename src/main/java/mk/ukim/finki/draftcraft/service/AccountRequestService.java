package mk.ukim.finki.draftcraft.service;


import mk.ukim.finki.draftcraft.domain.users.AccountRequestStatus;
import mk.ukim.finki.draftcraft.dto.AccountRequestDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateAccountRequestDto;

import java.util.List;

public interface AccountRequestService {

  AccountRequestDto createAccountRequest(CreateAccountRequestDto createAccountRequestDto);

  List<AccountRequestDto> findAll(AccountRequestStatus status, Integer page, Integer size);

  void confirmEmail(String token);

  AccountRequestDto acceptAccountRequest(Long id, AccountRequestStatus status);
}