package mk.ukim.finki.draftcraft.mapper;

import mk.ukim.finki.draftcraft.domain.model.user.Name;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.domain.model.user.UserRole;
import mk.ukim.finki.draftcraft.dto.LoginResponseDto;
import mk.ukim.finki.draftcraft.dto.UserDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateUserDto;
import mk.ukim.finki.draftcraft.dto.input.user.UpdateUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "image")
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "createUserDto", qualifiedByName = "mapFullName")
    @Mapping(target = "role", source = "createUserDto.role", qualifiedByName = "mapRole")
    User createUserDtoToEntity(CreateUserDto createUserDto);

    @Mapping(target = "name", source = "user", qualifiedByName = "mapFullName")
    List<UserDto> listToDto(List<User> usersList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.name", source = "updateUserDto", qualifiedByName = "mapFullName")
    User updateDtoToEntity(@MappingTarget User user, UpdateUserDto updateUserDto);

    @Named("mapFullName")
    default Name map(UpdateUserDto updateUserDto) {
        return new Name(updateUserDto.getName(), updateUserDto.getSurname());
    }

    @Named("mapFullName")
    default Name map(CreateUserDto createUserDto) {
        return new Name(createUserDto.getName(), createUserDto.getSurname());
    }

    @Named("mapRole")
    default UserRole mapRole(String role) {
        return UserRole.valueOf(role);
    }

    LoginResponseDto toLoginResponseDto(UserDto userDto, UserRole role);
}
