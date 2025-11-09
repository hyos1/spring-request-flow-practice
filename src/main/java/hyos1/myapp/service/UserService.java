package hyos1.myapp.service;

import hyos1.myapp.dto.request.SignUpRequest;
import hyos1.myapp.dto.request.UserUpdateRequest;
import hyos1.myapp.dto.response.UserResponse;
import hyos1.myapp.entity.User;
import hyos1.myapp.repository.user.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public UserResponse signUp(SignUpRequest request) {
        //이메일 중복 검증
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        String encodePassword = passwordEncoder.encode(request.getPassword());
        User user = User.createUser(
                request.getName(),
                request.getEmail(),
                encodePassword,
                request.getUserRole()
        );
        userRepository.save(user);

        return UserResponse.fromEntity(user);
    }

    /**
     * 단일 회원 조회
     */
    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다"));
        return UserResponse.fromEntity(user);
    }

    /**
     * [관리자용]전체 회원 조회
     */
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(u -> UserResponse.fromEntity(u))
                .collect(Collectors.toList());
    }

    /**
     * 회원 수정
     */
    @Transactional
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return UserResponse.fromEntity(user);
    }

    /**
     * 회원 탈퇴(Soft Delete)
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        if (user.isDeleted()) {
            throw new IllegalStateException("이미 탈퇴한 회원입니다.");
        }
        user.setDeleted(true);
    }
}