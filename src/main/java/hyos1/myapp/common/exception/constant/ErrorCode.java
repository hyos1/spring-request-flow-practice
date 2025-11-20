package hyos1.myapp.common.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // JWT 관련 예외
//    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
//    JWT_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
//    JWT_PROCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 처리 중 오류가 발생했습니다."),

    // 회원 예외 처리
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자입니다."),

    // 쿠폰 관련
    COUPON_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 쿠폰 이름입니다."),
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
    COUPON_DATE_INVALID(HttpStatus.BAD_REQUEST, "만료일은 시작일 이후여야 합니다."),
    COUPON_SOLD_OUT(HttpStatus.BAD_REQUEST, "쿠폰 수량이 모두 소진되었습니다."),
    COUPON_COUNT_INVALID(HttpStatus.BAD_REQUEST, "수량과 1인당 사용 횟수는 음수일 수 없습니다."),

    // 아이템 관련
    ITEM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이템입니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 아이템입니다."),
    ITEM_DELETE_NOT_FOUND(HttpStatus.NOT_FOUND, "삭제할 아이템이 존재하지 않습니다.");

    ;

    private final HttpStatus httpStatus;
    private final String message;
}