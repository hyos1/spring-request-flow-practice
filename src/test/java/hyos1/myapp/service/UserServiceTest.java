package hyos1.myapp.service;

import hyos1.myapp.common.UserRole;
import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import hyos1.myapp.dto.request.UserUpdateRequest;
import hyos1.myapp.dto.response.UserResponse;
import hyos1.myapp.entity.User;
import hyos1.myapp.repository.user.datajpa.UserDataRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // 순수 jpa
//    private UserRepository userRepository;
    // data jpa
    @Mock
    private UserDataRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User user;
    @BeforeEach
    void beforeEach() {
        user = User.createUser("userA", "test@naver.com", "1234", UserRole.ROLE_USER);
        user.setId(1L);
    }
    @Test
    void findById_정상조회() {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        UserResponse response = userService.findById(1L);

        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findById_예외발생() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(1L))
                .isInstanceOf(ClientException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    void updateUser_정상수정() {
        UserUpdateRequest request = new UserUpdateRequest("newEmail@naver.com", "1234", "newPass");

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.encode(request.getNewPassword())).willReturn(request.getNewPassword());
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);

        userService.updateUser(1L, 1L, request);

        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getPassword()).isEqualTo(request.getNewPassword());
    }

    @Test
    void updateUser_본인아님_예외발생() {
        UserUpdateRequest request = new UserUpdateRequest("newEmail@naver.com", "1234", "newPass");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.updateUser(2L, 1L, request))
                .isInstanceOf(ClientException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_OWNER);
    }

    @Test
    void updateUser_비밀번호틀리면_예외발생() {
        UserUpdateRequest request = new UserUpdateRequest("newEmail@naver.com", "1234", "newPass");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).willReturn(false);

        assertThatThrownBy(() -> userService.updateUser(1L, 1L, request))
                .isInstanceOf(ClientException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_PASSWORD);
    }

    @Test
    void updateUser_기존비밀번호로_변경시_예외발생() {
        UserUpdateRequest request = new UserUpdateRequest("newEmail@naver.com", "1234", "1234");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(true);

        assertThatThrownBy(() -> userService.updateUser(1L, 1L, request))
                .isInstanceOf(ClientException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PASSWORD_SAME_AS_OLD);
    }

    @Test
    void updateUser_중복이메일_예외발생() {
        UserUpdateRequest request = new UserUpdateRequest("newEmail@naver.com", "1234", "newPass");

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(false);
        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

        assertThatThrownBy(() -> userService.updateUser(1L, 1L, request))
                .isInstanceOf(ClientException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}