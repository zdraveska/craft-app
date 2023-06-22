package mk.ukim.finki.draftcraft.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.draftcraft.domain.enumeration.AccountRequestStatus;
import mk.ukim.finki.draftcraft.dto.AccountRequestDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateAccountRequestDto;
import mk.ukim.finki.draftcraft.service.AccountRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-requests")
@RequiredArgsConstructor
public class AccountRequestController {

  private final AccountRequestService accountRequestService;

  @PostMapping
  public ResponseEntity<AccountRequestDto> createAccountRequest(
      @Valid @RequestBody CreateAccountRequestDto createAccountRequestDto) {
    return ResponseEntity.ok().body(accountRequestService.createAccountRequest(createAccountRequestDto));
  }

  //  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<AccountRequestDto>> getAllAccountRequests(@RequestParam(required = false) AccountRequestStatus status) {
    return ResponseEntity.ok().body(accountRequestService.findAll(status));
  }

  @PutMapping(value = "/confirm-email")
  public ResponseEntity<Void> confirmEmail(@RequestParam String token) {
    accountRequestService.confirmEmail(token);
    return ResponseEntity.ok().build();
  }

//  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping(value = "/{id}")
  public ResponseEntity<AccountRequestDto> confirmAccountRequest(
      @RequestParam AccountRequestStatus status,
      @PathVariable Long id) {
    return ResponseEntity.ok().body(accountRequestService.handleAccountRequest(id, status));
  }

}