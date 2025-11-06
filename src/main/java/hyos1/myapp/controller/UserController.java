package hyos1.myapp.controller;

import hyos1.myapp.dto.request.UserSignUpRequest;
import hyos1.myapp.dto.request.UserUpdateRequest;
import hyos1.myapp.dto.response.UserResponse;
import hyos1.myapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping
    public ResponseEntity<UserResponse> singUp(@Valid @RequestBody UserSignUpRequest request) {
        UserResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    //단건 회원 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long userId) {
        UserResponse response = userService.findById(userId);
        return ResponseEntity.ok(response);
    }

    //전체 회원 조회
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    //회원 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
                                                   @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(userId, request);
        return ResponseEntity.ok(response);
    }

    //회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
