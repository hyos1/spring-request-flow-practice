package hyos1.myapp.controller;

import hyos1.myapp.dto.request.LoginRequest;
import hyos1.myapp.dto.request.SignUpRequest;
import hyos1.myapp.dto.response.UserResponse;
import hyos1.myapp.entity.User;
import hyos1.myapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그인 -> JWT 발급
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        String bearerToken = authService.login(request);
        return ResponseEntity.ok().header("Authorization", bearerToken).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<List<UserResponse>> findAll() {
        List<User> users = authService.findAll();
        List<UserResponse> results = users.stream().map(u -> UserResponse.fromEntity(u))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }
}
