package mk.ukim.finki.draftcraft.mapper;

import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.domain.enumeration.UserRole;
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
    @Mapping(target = "role", source = "createUserDto.role", qualifiedByName = "mapRole")
    User createUserDtoToEntity(CreateUserDto createUserDto);

    List<UserDto> listToDto(List<User> usersList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    User updateDtoToEntity(@MappingTarget User user, UpdateUserDto updateUserDto);

    @Named("mapRole")
    default UserRole mapRole(String role) {
        return UserRole.valueOf(role);
    }

    LoginResponseDto toLoginResponseDto(UserDto userDto, UserRole role);
}
