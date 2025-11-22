package hyos1.myapp.service;

import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import hyos1.myapp.dto.request.SignUpRequest;
import hyos1.myapp.dto.request.UserUpdateRequest;
import hyos1.myapp.dto.response.UserResponse;
import hyos1.myapp.entity.User;
import hyos1.myapp.repository.user.datajpa.UserDataRepository;
import hyos1.myapp.repository.user.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

//    private final UserRepository userRepository;
    private final UserDataRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 단일 회원 조회
     */
    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND));

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
    public UserResponse updateUser(Long authUserId, Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND));

        // 아이디로 본인 확인
        if (!authUserId.equals(userId)) {
            throw new ClientException(ErrorCode.USER_NOT_OWNER);
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ClientException(ErrorCode.INVALID_PASSWORD);
        }

        // 새 비밀번호가 기존 비밀번호와 같은지 확인
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new ClientException(ErrorCode.PASSWORD_SAME_AS_OLD);
        }

        // 같은 이메일 & 중복 이메일 확인
        if (user.getEmail().equals(request.getEmail()) || userRepository.existsByEmail(request.getEmail())) {
            throw new ClientException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        user.updateEmailPassword(request.getEmail(), passwordEncoder.encode(request.getNewPassword()));
        return UserResponse.fromEntity(user);
    }

    /**
     * 회원 탈퇴(Soft Delete)
     */
    @Transactional
    public void deleteUser(Long authUserId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND));

        // 아이디로 본인 확인
        if (!authUserId.equals(userId)) {
            throw new AccessDeniedException("본인만 탈퇴할 수 있습니다.");
        }

        if (user.isDeleted()) {
            throw new ClientException(ErrorCode.USER_ALREADY_DELETED);
        }
        user.setDeleted(true);
    }
}