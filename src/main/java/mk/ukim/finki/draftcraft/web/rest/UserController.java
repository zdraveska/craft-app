//package mk.ukim.finki.draftcraft.web.rest;
//
//import com.example.demo.domain.exceptions.ImageException;
//import com.example.demo.domain.users.UserRole;
//import com.example.demo.dto.UserDto;
//import com.example.demo.dto.input.user.ChangeUserPasswordDto;
//import com.example.demo.dto.input.user.CreateUserDto;
//import com.example.demo.dto.input.user.PasswordDto;
//import com.example.demo.dto.input.user.UpdateUserDto;
//import com.example.demo.service.UserService;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
////    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping(value = "/users")
//    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
//        return ResponseEntity.ok().body(this.userService.createUser(createUserDto));
//    }
//
////    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @GetMapping(value = "/users/{id}")
//    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
//        return ResponseEntity.ok().body(this.userService.findById(id));
//    }
//
////    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @GetMapping(value = "/users")
//    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(defaultValue = "0") Integer page,
//                                                     @RequestParam(defaultValue = "7") Integer size) {
//        return ResponseEntity.ok().body(this.userService.listAllUsers(page, size));
//    }
//
////    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @PutMapping(value = "/users/{id}")
//    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDto updateUserDto) {
//        return ResponseEntity.ok().body(userService.updateUser(id, updateUserDto));
//    }
//
////    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @GetMapping(value = "/users/me")
//    public ResponseEntity<UserDto> getUserProfile() {
//        return ResponseEntity.ok().body(this.userService.getMyProfile());
//    }
//
////    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping(value = "/users/roles")
//    public ResponseEntity<List<UserRole>> getAllRoles() {
//        return ResponseEntity.ok().body(this.userService.listAllRoles());
//    }
//
//    @PostMapping("/users/request-reset-password")
//    public ResponseEntity<Void> requestResetPassword(@RequestParam String email) {
//        userService.requestResetPassword(email);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping(value = "/users/reset-password")
//    public ResponseEntity<UserDto> resetPassword(@Valid @RequestBody PasswordDto passwordDto, String token) {
//        return ResponseEntity.ok().body(userService.resetPassword(passwordDto, token));
//    }
//
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @PutMapping(value = "/users/change-password")
//    public ResponseEntity<UserDto> changePassword(@Valid @RequestBody ChangeUserPasswordDto changeUserPasswordDto) {
//        return ResponseEntity.ok().body(userService.changePassword(changeUserPasswordDto));
//    }
//
//    @PostMapping(value = "/users/password-complexity")
//    public ResponseEntity<Integer> checkPasswordComplexity(@RequestBody PasswordDto passwordDto) {
//        return ResponseEntity.ok().body(userService.checkPasswordComplexity(passwordDto));
//    }
//
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @PostMapping(value = "/users/{id}/image", consumes = {"multipart/form-data"})
//    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestPart MultipartFile file) {
//        userService.uploadImage(id, file);
//        return ResponseEntity.ok().build();
//    }
//
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @GetMapping(value = "/users/{id}/image")
//    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) {
//        UserDto userDto = userService.findById(id);
//        if (userDto.getImage() == null) {
//            throw new ImageException(userDto.getId());
//        }
//        return createResourceResponseFromInputStream(userDto.getImage().getImageName(),
//                new ByteArrayInputStream(userDto.getImage().getImage()),
//                MediaType.parseMediaType(userDto.getImage().getImageContentType()));
//    }
//
//    private ResponseEntity<Resource> createResourceResponse(Resource resource, String fileName, MediaType fileType) {
//        return ResponseEntity.ok()
//                .headers(getDefaultHeader(fileName))
//                .contentType(fileType)
//                .body(resource);
//    }
//
//    @NotNull
//    private ResponseEntity<Resource> createResourceResponseFromInputStream(String fileName, InputStream fileInputStream,
//                                                                           MediaType fileType) {
//        return createResourceResponse(new InputStreamResource(fileInputStream), fileName, fileType);
//    }
//
//    private HttpHeaders getDefaultHeader(String fileName) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//        return headers;
//    }
//
//}