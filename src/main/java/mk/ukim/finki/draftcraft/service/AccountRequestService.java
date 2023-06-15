package mk.ukim.finki.draftcraft.service;


import mk.ukim.finki.draftcraft.domain.model.user.AccountRequestStatus;
import mk.ukim.finki.draftcraft.dto.AccountRequestDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateAccountRequestDto;

import java.util.List;

public interface AccountRequestService {

  AccountRequestDto createAccountRequest(CreateAccountRequestDto createAccountRequestDto);

  void confirmEmail(String token);

  List<AccountRequestDto> findAll();

  AccountRequestDto acceptAccountRequest(Long id, AccountRequestStatus status);
}