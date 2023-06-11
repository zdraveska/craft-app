package mk.ukim.finki.draftcraft.mapper;

import mk.ukim.finki.draftcraft.domain.users.Name;
import mk.ukim.finki.draftcraft.domain.users.User;
import mk.ukim.finki.draftcraft.domain.users.UserRole;
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

    @Mapping(target = "name", source = "createUserDto", qualifiedByName = "createFullName")
    User createUserDtoToEntity(CreateUserDto createUserDto);

    @Mapping(target = "name", source = "user", qualifiedByName = "createFullName")
    List<UserDto> listToDto(List<User> usersList);

    @Mapping(target = "user.name", source = "updateUserDto", qualifiedByName = "createFullName")
    User updateDtoToEntity(@MappingTarget User user, UpdateUserDto updateUserDto);

    @Named("createFullName")
    default Name map(UpdateUserDto updateUserDto) {
        return new Name(updateUserDto.getName(), updateUserDto.getSurname());
    }

    @Named("createFullName")
    default Name map(CreateUserDto createUserDto) {
        return new Name(createUserDto.getName(), createUserDto.getSurname());
    }


    LoginResponseDto toLoginResponseDto(UserDto userDto, UserRole role);
}
