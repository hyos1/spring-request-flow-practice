package hyos1.myapp.dto;

import lombok.Getter;

@Getter
public class UserUpdateDto {

    private String name;
    private String email;

    public UserUpdateDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
