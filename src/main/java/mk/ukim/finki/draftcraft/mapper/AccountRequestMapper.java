package mk.ukim.finki.draftcraft.mapper;

import mk.ukim.finki.draftcraft.domain.model.user.AccountRequest;
import mk.ukim.finki.draftcraft.domain.enumeration.UserRole;
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

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", constant = "PENDING")
  @Mapping(target = "emailConfirmed", constant = "false")
  @Mapping(target = "createdDate", source = "date")
  @Mapping(target = "role", source = "createAccountRequestDto", qualifiedByName = "mapRole")
  AccountRequest toEntity(CreateAccountRequestDto createAccountRequestDto, LocalDate date);

  List<AccountRequestDto> listToDto(List<AccountRequest> accountRequests);

  @Mapping(target = "role", source = "accountRequest", qualifiedByName = "mapRole")
  CreateUserDto toCreateUserDto(AccountRequest accountRequest);

  @Named("mapRole")
  default UserRole mapRole(CreateAccountRequestDto createAccountRequestDto) {
    return UserRole.valueOf(createAccountRequestDto.getRole());
  }

  @Named("mapRole")
  default String mapRole(AccountRequest accountRequest) {
    return String.valueOf(accountRequest.getRole());
  }

}