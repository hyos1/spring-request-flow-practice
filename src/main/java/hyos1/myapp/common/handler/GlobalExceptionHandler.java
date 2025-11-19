package hyos1.myapp.common.handler;

import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.common.exception.ServerException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import hyos1.myapp.common.exception.constant.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler // (ClientException.class)생략 가능
    public ResponseEntity<ErrorResponse> clientExHandler(ClientException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("clientExHandler ex", e);
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> serverExHandler(ServerException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("clientExHandler ex", e);
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.from(errorCode));
    }
}