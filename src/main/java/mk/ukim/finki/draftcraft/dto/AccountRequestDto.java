package mk.ukim.finki.draftcraft.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.draftcraft.domain.users.AccountRequestStatus;
import mk.ukim.finki.draftcraft.domain.users.UserRole;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequestDto {

  Long id;

  String name;

  String surname;

  String email;

  String phoneNumber;

  UserRole role;

  AccountRequestStatus status;

  LocalDate createdDate;

}