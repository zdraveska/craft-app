package mk.ukim.finki.draftcraft.service;

import mk.ukim.finki.draftcraft.domain.users.User;
import mk.ukim.finki.draftcraft.domain.users.UserRole;
import mk.ukim.finki.draftcraft.dto.UserDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateUserDto;
import mk.ukim.finki.draftcraft.dto.input.user.PasswordDto;
import mk.ukim.finki.draftcraft.dto.input.user.UpdateUserDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService {

  UserDto createUser(CreateUserDto userDto);

  UserDto findById(Long id);

  List<UserDto> listAllUsers(Integer page, Integer size);

  UserDto updateUser(Long id, UpdateUserDto updateUserDto);


  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

  List<UserRole> listAllRoles();

//  UserDto getMyProfile();

//  void requestResetPassword(String email);

//  UserDto resetPassword(PasswordDto passwordDto, String token);

//  UserDto changePassword(ChangeUserPasswordDto changeUserPasswordDto);

  @Cacheable(cacheNames = {"user"})
  UserDto getMyProfile();

  Integer checkPasswordComplexity(PasswordDto passwordDto);

  UserDetailsService userDetailsService();

//  void uploadImage(Long id, MultipartFile multipartFile);

}