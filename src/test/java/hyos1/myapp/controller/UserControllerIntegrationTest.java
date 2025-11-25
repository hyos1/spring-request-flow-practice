package hyos1.myapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hyos1.myapp.common.UserRole;
import hyos1.myapp.common.exception.constant.ErrorCode;
import hyos1.myapp.config.JwtUtil;
import hyos1.myapp.dto.request.UserUpdateRequest;
import hyos1.myapp.entity.User;
import hyos1.myapp.repository.user.jpa.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User admin;
    private User user;
    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        admin = User.createUser("관리자", "admin@naver.com", passwordEncoder.encode("1234"), UserRole.ROLE_ADMIN);
        admin = userRepository.save(admin);

        user = User.createUser("회원", "user@naver.com", passwordEncoder.encode("1234"), UserRole.ROLE_USER);
        user = userRepository.save(user);

        // JWT 토큰 발급
        adminToken = jwtUtil.createToken(admin.getId(), admin.getEmail(), admin.getUserRole());
        userToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());
    }

    @Test
    void 본인_조회_성공() throws Exception {
        mockMvc.perform(get("/users/me")
                        .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.userRole").value(user.getUserRole().name()));
    }

    @Test
    void 회원_전체_조회() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(jsonPath("$[1].email").exists());
    }
    @Test
    void 회원_전체_조회_일반회원_실패() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", userToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorName").value(ErrorCode.ACCESS_DENIED.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCESS_DENIED.getMessage()));
    }

    @Test
    void 회원_수정_성공() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("newEmail@naver.com", "1234", "newPass");
        mockMvc.perform(patch("/users/{userId}", user.getId())
                        .header("Authorization", userToken)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(request.getEmail()));
    }

    @Test
    void 회원_수정_비밀번호불일치_실패() throws Exception{
        UserUpdateRequest request = new UserUpdateRequest("newEmail@naver.com", "wrongPassword", "newPass");
        mockMvc.perform(patch("/users/{userId}", user.getId())
                        .header("Authorization", userToken)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorName").value(ErrorCode.INVALID_PASSWORD.name()));
    }

    @Test
    void 회원_삭제_성공() throws Exception {
        mockMvc.perform(delete("/users/{userId}", user.getId())
                        .header("Authorization", userToken)
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }
    @Test
    void 회원_삭제_본인아님_실패() throws Exception {
        mockMvc.perform(delete("/users/{userId}", user.getId())
                        .header("Authorization", adminToken)
                        .contentType("application/json"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorName").value(ErrorCode.USER_NOT_OWNER.name()));
    }
    @Test
    void 존재하지_않는_회원_삭제_실패() throws Exception {
        mockMvc.perform(delete("/users/{userId}", 999L)
                        .header("Authorization", userToken)
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorName").value(ErrorCode.USER_NOT_FOUND.name()));
    }
}