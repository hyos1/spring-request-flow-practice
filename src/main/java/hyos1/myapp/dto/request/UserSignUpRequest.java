package hyos1.myapp.dto.request;

import hyos1.myapp.common.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserSignUpRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank
    private UserType userType;
}
