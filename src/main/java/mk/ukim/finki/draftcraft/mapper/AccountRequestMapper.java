package mk.ukim.finki.draftcraft.mapper;

import mk.ukim.finki.draftcraft.domain.users.AccountRequest;
import mk.ukim.finki.draftcraft.domain.users.UserRole;
import mk.ukim.finki.draftcraft.dto.AccountRequestDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateAccountRequestDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountRequestMapper {

  AccountRequestDto toDto(AccountRequest accountRequest);

  @Mapping(target = "status", constant = "PENDING")
  //todo return false
  @Mapping(target = "emailConfirmed", constant = "true")
  @Mapping(target = "createdDate", source = "date")
  @Mapping(target = "role", source = "createAccountRequestDto", qualifiedByName = "mapRole")
  AccountRequest toEntity(CreateAccountRequestDto createAccountRequestDto, LocalDate date);

  List<AccountRequestDto> listToDto(List<AccountRequest> accountRequests);

  CreateUserDto toCreateUserDto(AccountRequest accountRequest);

  @Named("mapRole")
  default UserRole mapRole(CreateAccountRequestDto createAccountRequestDto) {
    return UserRole.valueOf(createAccountRequestDto.getRole());
  }

}