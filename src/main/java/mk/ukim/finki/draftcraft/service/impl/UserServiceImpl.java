package mk.ukim.finki.draftcraft.service.impl;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.draftcraft.domain.enumeration.EmailType;
import mk.ukim.finki.draftcraft.domain.enumeration.UserRole;
import mk.ukim.finki.draftcraft.domain.exceptions.DuplicateEmailException;
import mk.ukim.finki.draftcraft.domain.exceptions.IncorrectPassword;
import mk.ukim.finki.draftcraft.domain.exceptions.InvalidPasswordException;
import mk.ukim.finki.draftcraft.domain.exceptions.UserNotFoundException;
import mk.ukim.finki.draftcraft.domain.model.common.Image;
import mk.ukim.finki.draftcraft.domain.model.common.UrlToken;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.dto.EmailDto;
import mk.ukim.finki.draftcraft.dto.UserDto;
import mk.ukim.finki.draftcraft.dto.input.user.ChangeUserPasswordDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateUserDto;
import mk.ukim.finki.draftcraft.dto.input.user.PasswordDto;
import mk.ukim.finki.draftcraft.dto.input.user.UpdateUserDto;
import mk.ukim.finki.draftcraft.mapper.UserMapper;
import mk.ukim.finki.draftcraft.repository.ImageRepository;
import mk.ukim.finki.draftcraft.repository.UserRepository;
import mk.ukim.finki.draftcraft.service.EmailService;
import mk.ukim.finki.draftcraft.service.TokenService;
import mk.ukim.finki.draftcraft.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static mk.ukim.finki.draftcraft.util.EmailUtil.generateEmail;
import static mk.ukim.finki.draftcraft.util.ImageUtil.checkIfImageIsValid;
import static mk.ukim.finki.draftcraft.util.ImageUtil.compressMultipartFile;
import static mk.ukim.finki.draftcraft.util.PermissionUtil.*;
import static mk.ukim.finki.draftcraft.util.ValidationUtil.checkIfAlphabetic;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ImageRepository imageRepository;
  private final UserMapper userMapper;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  static final String RESET_PASSWORD_PATH = "/api/users/request-password?token=";

public UserServiceImpl(UserRepository userRepository,
                       ImageRepository imageRepository,
                       UserMapper userMapper,
                       EmailService emailService,
                       @Lazy PasswordEncoder passwordEncoder,
                       TokenService tokenService
                       ){
  this.userRepository = userRepository;
  this.imageRepository = imageRepository;
  this.userMapper = userMapper;
  this.emailService = emailService;
  this.passwordEncoder = passwordEncoder;
  this.tokenService = tokenService;
}

  @CacheEvict(cacheNames = {"users"}, allEntries = true)
  @Override
  public UserDto createUser(CreateUserDto createUserDto) {
    if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
      throw new DuplicateEmailException(createUserDto.getEmail());
    }

    checkIfAlphabetic(createUserDto.getName(), createUserDto.getSurname());

    User user = userMapper.createUserDtoToEntity(createUserDto);
    user = userRepository.save(user);
    log.debug("User {} created", user);

    String tokenValue = tokenService.createUrlToken(null, user).getToken();
    String url = tokenService.generateEmailUrl(tokenValue, RESET_PASSWORD_PATH);

    EmailDto emailDto = generateEmail(createUserDto.getEmail(), createUserDto.getName(), createUserDto.getSurname(),
        EmailType.CREATE_USER, url);
    emailService.sendSimpleEmail(emailDto, EmailType.CREATE_USER);
    return userMapper.toDto(user);
  }

  @Cacheable(cacheNames = {"users"}, key = "#id")
  @Override
  public UserDto findById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    log.info("Find user by id: " + id);
    return userMapper.toDto(user);
  }

  @Cacheable(cacheNames = {"users"})
  @Override
  public List<UserDto> listAllUsers() {
    log.info("List all users");
    return userMapper.listToDto(userRepository.findAllByOrderByNameAsc().stream().toList());
  }

  @Cacheable(cacheNames = {"users"}, key = "#email")
  @Override
  public Optional<User> findByEmail(String email) {
    log.info("Finding user with email {}", email);
    return userRepository.findByEmail(email);
  }

  @Override
  public List<UserRole> listAllRoles() {
    log.info("List all roles");
    return Arrays.stream(UserRole.values()).collect(Collectors.toList());
  }

  @Cacheable(cacheNames = {"user"})
  @Override
  public UserDto getMyProfile() {
    String username = getUsername();
    log.info("Get my profile");
    User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException(username));
    return userMapper.toDto(user);
  }

  @Override
  public UserDto changePassword(ChangeUserPasswordDto changeUserPasswordDto) {
    String email = getUsername();
    User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    if (!passwordEncoder.matches(changeUserPasswordDto.getOldPassword(), user.getPassword())) {
      throw new IncorrectPassword();
    }

    user.setPassword(passwordEncoder.encode(changeUserPasswordDto.getNewPassword()));
    log.info("Change password");
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  public Integer checkPasswordComplexity(PasswordDto passwordDto) {
    Zxcvbn zxcvbn = new Zxcvbn();
    String password = passwordDto.getPassword();
    Strength strength = zxcvbn.measure(password);
    int passwordScore = strength.getScore();
    if (password.length() < 8 || passwordScore < 2) {
      throw new InvalidPasswordException();
    }
    log.info("Password checked for complexity");
    return passwordScore;
  }

  @CacheEvict(cacheNames = {"users", "user"}, key = "#id", allEntries = true)
  @Override
  public void uploadImage(Long id, MultipartFile multipartFile) {
    log.info("Upload image");
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));

    checkIfImageIsValid(multipartFile);
    checkUserUploadImagePermission(user.getEmail(), id);

    Image image = Image.builder()
            .image(compressMultipartFile(multipartFile))
            .imageContentType(multipartFile.getContentType())
            .imageName(multipartFile.getOriginalFilename())
            .build();
    image = imageRepository.save(image);
    user.setImage(image);
    userRepository.save(user);
  }

  @CacheEvict(cacheNames = {"users", "user"}, key = "#id", allEntries = true)
  @Override
  public UserDto updateUser(Long id, UpdateUserDto updateUserDto) {
    User user = this.userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
    checkIfAlphabetic(updateUserDto.getName(), updateUserDto.getSurname());
    checkUserEditPermission(user.getEmail(), id, updateUserDto.getEmail());
    user = userMapper.updateDtoToEntity(user, updateUserDto);
    userRepository.save(user);
    log.info("User {} updated", user);
    return userMapper.toDto(user);
  }

  @Override
  public void requestResetPassword(String email) {
    log.info("Request for reset password");
    User user = findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    String tokenValue = tokenService.createUrlToken(null, user).getToken();
    String url = tokenService.generateEmailUrl(tokenValue, RESET_PASSWORD_PATH);
    EmailDto emailDto = generateEmail(email, user.getName(), user.getSurname(), EmailType.RESET_PASSWORD, url);
    emailService.sendSimpleEmail(emailDto, EmailType.RESET_PASSWORD);
  }

  @Override
  public UserDto requestPassword(PasswordDto passwordDto, String tokenValue) {
    UrlToken token = tokenService.findByToken(tokenValue);
    User user = token.getUser();
    user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
    log.info("Set password");
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  public UserDetailsService userDetailsService() {
    return username -> {
      try {
        return userRepository.findByEmail(username).orElseThrow();
      } catch (UsernameNotFoundException ex) {
        throw new UserNotFoundException();
      }
    };
  }
}
