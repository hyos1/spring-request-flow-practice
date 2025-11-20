package hyos1.myapp.common.exception;

import hyos1.myapp.common.exception.constant.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private String errorName;
    private int status;
    private String message;

    public ErrorResponse(String errorName, int status, String message) {
        this.errorName = errorName;
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse from(ErrorCode code) {
        return new ErrorResponse(code.name(), code.getHttpStatus().value(), code.getMessage());
    }
}