package hyos1.myapp.dto.request;

import lombok.Getter;

@Getter
public class UserUpdateRequestDtoWithJdbc {

    private String name;
    private String email;

    public UserUpdateRequestDtoWithJdbc(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
