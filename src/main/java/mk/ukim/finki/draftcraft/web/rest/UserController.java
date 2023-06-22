package mk.ukim.finki.draftcraft.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.draftcraft.domain.enumeration.UserRole;
import mk.ukim.finki.draftcraft.dto.UserDto;
import mk.ukim.finki.draftcraft.dto.input.user.ChangeUserPasswordDto;
import mk.ukim.finki.draftcraft.dto.input.user.CreateUserDto;
import mk.ukim.finki.draftcraft.dto.input.user.PasswordDto;
import mk.ukim.finki.draftcraft.dto.input.user.UpdateUserDto;
import mk.ukim.finki.draftcraft.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //TODO
    //    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.ok().body(this.userService.createUser(createUserDto));
    }

    //TODO
    //    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.userService.findById(id));
    }

    //TODO
    //    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok().body(this.userService.listAllUsers());
    }

    //TODO
    //    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok().body(userService.updateUser(id, updateUserDto));
    }

    //TODO
    //    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(value = "/me")
    public ResponseEntity<UserDto> getUserProfile() {
        return ResponseEntity.ok().body(this.userService.getMyProfile());
    }

    //TODO
    //    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/roles")
    public ResponseEntity<List<UserRole>> getAllRoles() {
        return ResponseEntity.ok().body(this.userService.listAllRoles());
    }

    //TODO
    @PostMapping("/request-reset-password")
    public ResponseEntity<Void> requestResetPassword(@RequestParam String email) {
        userService.requestResetPassword(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/request-password")
    public ResponseEntity<UserDto> requestPassword(@Valid @RequestBody PasswordDto passwordDto, String token) {
        return ResponseEntity.ok().body(userService.requestPassword(passwordDto, token));
    }

    //TODO
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping(value = "/change-password")
    public ResponseEntity<UserDto> changePassword(@Valid @RequestBody ChangeUserPasswordDto changeUserPasswordDto) {
        return ResponseEntity.ok().body(userService.changePassword(changeUserPasswordDto));
    }

    //TODO
    @PostMapping(value = "/password-complexity")
    public ResponseEntity<Integer> checkPasswordComplexity(@RequestBody PasswordDto passwordDto) {
        return ResponseEntity.ok().body(userService.checkPasswordComplexity(passwordDto));
    }

    //TODO
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(value = "/{id}/image", consumes = {"multipart/form-data"})
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestPart MultipartFile file) {
        userService.uploadImage(id, file);
        return ResponseEntity.ok().build();
    }
}