package mk.ukim.finki.draftcraft.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.draftcraft.domain.users.AccountRequestStatus;
import mk.ukim.finki.draftcraft.dto.AccountRequestDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateAccountRequestDto;
import mk.ukim.finki.draftcraft.service.AccountRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountRequestController {

  private final AccountRequestService accountRequestService;

  @PostMapping(value = "/account-requests")
  public ResponseEntity<AccountRequestDto> createAccountRequest(
      @Valid @RequestBody CreateAccountRequestDto createAccountRequestDto) {
    return ResponseEntity.ok().body(accountRequestService.createAccountRequest(createAccountRequestDto));
  }

  //TODO
  //  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping(value = "/account-requests")
  public ResponseEntity<List<AccountRequestDto>> getAllAccountRequests(
      @RequestParam(required = false) AccountRequestStatus status,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "7") Integer size) {
    return ResponseEntity.ok().body(accountRequestService.findAll(status, page, size));
  }

  @PutMapping(value = "/account-requests/confirm-email")
  public ResponseEntity<Void> confirmEmail(@RequestParam String token) {
    accountRequestService.confirmEmail(token);
    return ResponseEntity.ok().build();
  }

//  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping(value = "/account-requests/{id}")
  public ResponseEntity<AccountRequestDto> confirmAccountRequest(
      @RequestParam AccountRequestStatus status,
      @PathVariable Long id) {
    return ResponseEntity.ok().body(accountRequestService.acceptAccountRequest(id, status));
  }

}