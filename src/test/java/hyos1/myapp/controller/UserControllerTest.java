package hyos1.myapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hyos1.myapp.common.UserRole;
import hyos1.myapp.config.CustomAuthenticationEntryPoint;
import hyos1.myapp.config.JwtAuthenticationToken;
import hyos1.myapp.config.JwtUtil;
import hyos1.myapp.config.SecurityConfig;
import hyos1.myapp.dto.request.UserUpdateRequest;
import hyos1.myapp.dto.response.UserResponse;
import hyos1.myapp.entity.AuthUser;
import hyos1.myapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@AutoConfigureMockMvc(addFilters = false) // security 필터 제거

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtUtil.class, CustomAuthenticationEntryPoint.class})
class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private JwtAuthenticationToken adminAuthenticationToken;
    private JwtAuthenticationToken userAuthenticationToken;

    private AuthUser admin;
    private AuthUser user;

    @BeforeEach
    void setUp() {
        admin = new AuthUser(1L, "admin@example.com", UserRole.ROLE_ADMIN);
        adminAuthenticationToken = new JwtAuthenticationToken(admin);

        user = new AuthUser(2L, "user@example.com", UserRole.ROLE_USER);
        userAuthenticationToken = new JwtAuthenticationToken(user);
    }

    @Test
    void findMe_success() throws Exception {
        // given

        UserResponse response = new UserResponse(
                user.getUserId(), "홍길동", user.getEmail(), UserRole.ROLE_USER
        );

        BDDMockito.when(userService.findById(user.getUserId()))
                .thenReturn(response);

        // when & then
        mockMvc.perform(get("/users/me")
                        .with(authentication(userAuthenticationToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getUserId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void findAll_success() throws Exception {
        List<UserResponse> list = List.of(new UserResponse(
                user.getUserId(), "홍길동", user.getEmail(), UserRole.ROLE_USER
        ));
        BDDMockito.when(userService.findAll()).thenReturn(list);

        mockMvc.perform(get("/users").with(authentication(adminAuthenticationToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(user.getEmail()));
    }

    @Test
    void findAll_fail() throws Exception {
        mockMvc.perform(get("/users").with(authentication(userAuthenticationToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUser() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("newEmail@naver.com", "1234", "12345");

        UserResponse response = new UserResponse(user.getUserId(), "기존이름", request.getEmail(), UserRole.ROLE_USER);
        BDDMockito.when(userService.updateUser(user.getUserId(), user.getUserId(), request))
                .thenReturn(response);

        mockMvc.perform(patch("/users/{userId}", user.getUserId())
                        .with(authentication(userAuthenticationToken))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}