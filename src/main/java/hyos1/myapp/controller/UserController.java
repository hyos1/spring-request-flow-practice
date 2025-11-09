package hyos1.myapp.controller;

import hyos1.myapp.dto.request.SignUpRequest;
import hyos1.myapp.dto.request.UserUpdateRequest;
import hyos1.myapp.dto.response.UserResponse;
import hyos1.myapp.entity.AuthUser;
import hyos1.myapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //단건 회원 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> findById(@AuthenticationPrincipal AuthUser authUser) {
        UserResponse response = userService.findById(authUser.getUserId());
        return ResponseEntity.ok(response);
    }

    //전체 회원 조회
    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    //회원 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
                                                   @Valid @RequestBody UserUpdateRequest request,
                                                   @AuthenticationPrincipal AuthUser authUser) {
        if (!authUser.getUserId().equals(userId) &&
                !authUser.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        UserResponse response = userService.updateUser(userId, request);
        return ResponseEntity.ok(response);
    }

    //회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId,
                                           @AuthenticationPrincipal AuthUser authUser) {
        if (!authUser.getUserId().equals(userId) &&
                !authUser.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
