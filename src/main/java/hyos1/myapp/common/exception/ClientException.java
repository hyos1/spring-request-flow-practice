package hyos1.myapp.common.exception;

import hyos1.myapp.common.exception.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private final ErrorCode errorCode;

    public ClientException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}