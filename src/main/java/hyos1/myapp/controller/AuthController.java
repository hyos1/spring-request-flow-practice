package hyos1.myapp.controller;

import hyos1.myapp.dto.request.LoginRequest;
import hyos1.myapp.dto.request.SignUpRequest;
import hyos1.myapp.dto.response.UserResponse;
import hyos1.myapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입 ✓
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignUpRequest request) {
        UserResponse signup = authService.signup(request);
        return ResponseEntity.status(HttpStatus.OK).body(signup);
    }

    // 로그인 -> JWT 발급 ✓
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        String bearerToken = authService.login(request);
        return ResponseEntity.ok().header("Authorization", bearerToken).build();
    }
}
