package hyos1.myapp.common.handler;

import hyos1.myapp.common.exception.ClientException;
import hyos1.myapp.common.exception.ErrorResponse;
import hyos1.myapp.common.exception.ServerException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> methodArgumentNotValidExHandler(MethodArgumentNotValidException e) {
        log.error("ValidationHandler ex", e);

        Map<String, String> errors = e.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<String> exceptionHandler(Exception e) {
        log.error("clientExHandler ex", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("내부 오류");
    }
}