package hyos1.myapp.service;

import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.config.JwtUtil;
import hyos1.myapp.dto.request.LoginRequest;
import hyos1.myapp.dto.request.SignUpRequest;
import hyos1.myapp.entity.User;
import hyos1.myapp.repository.user.datajpa.UserDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hyos1.myapp.common.exception.constant.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

//    private final UserJpaRepository userRepository; // 순수 jpa
    private final UserDataRepository userRepository; // data jpa
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    @Transactional
    public void signup(SignUpRequest request) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ClientException(EMAIL_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 유저 생성 및 저장
        User user = User.createUser(
                request.getName(),
                request.getEmail(),
                encodedPassword,
                request.getUserRole()
        );
        userRepository.save(user);
    }

    // 로그인
    public String login(LoginRequest request) {
        // 이메일 검증
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new ClientException(USER_NOT_FOUND)
        );

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ClientException(INVALID_PASSWORD);
        }
        // JWT 생성 후 반환
        return jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());
    }
}