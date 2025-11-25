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
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 UserRole입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "관리자 전용 기능입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "토큰이 필요한 기능입니다."),

    // 회원 예외 처리
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    USER_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    PASSWORD_SAME_AS_OLD(HttpStatus.BAD_REQUEST, "같은 비밀번호로는 변경할 수 없습니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자입니다."),
    USER_NOT_OWNER(HttpStatus.FORBIDDEN, "본인 정보만 수정 가능합니다."),

    // 쿠폰 예외 처리
    COUPON_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 쿠폰 이름입니다."),
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
    COUPON_DATE_INVALID(HttpStatus.BAD_REQUEST, "만료일은 시작일 이후여야 합니다."),
    COUPON_SOLD_OUT(HttpStatus.BAD_REQUEST, "쿠폰 수량이 모두 소진되었습니다."),
    COUPON_COUNT_INVALID(HttpStatus.BAD_REQUEST, "수량과 1인당 사용 횟수는 음수일 수 없습니다."),
    COUPON_USAGE_EXHAUSTED(HttpStatus.BAD_REQUEST, "쿠폰 사용 가능 횟수를 모두 소진했습니다."),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "만료된 쿠폰입니다."),
    COUPON_NOT_OWNER(HttpStatus.UNAUTHORIZED, "본인의 쿠폰만 사용할 수 있습니다."),

    USER_COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "발급받지 않은 쿠폰입니다."),
    USER_COUPON_NOT_FOUND_ADMIN(HttpStatus.NOT_FOUND, "사용자 쿠폰을 찾을 수 없습니다."),

    // 아이템 예외 처리
    ITEM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이템입니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 아이템입니다."),
    ITEM_DELETE_NOT_FOUND(HttpStatus.NOT_FOUND, "삭제할 아이템이 존재하지 않습니다."),
    ITEM_STOCK_EMPTY(HttpStatus.BAD_REQUEST, "상품 재고가 부족합니다."),

    // 주문 예외 처리
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
    ORDER_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소된 주문입니다."),
    COUPON_NOT_ISSUABLE_NOW(HttpStatus.BAD_REQUEST, "발급 가능한 날짜가 아닙니다."),
    COUPON_QUANTITY_EMPTY(HttpStatus.BAD_REQUEST, "쿠폰 수량이 모두 소진되었습니다."),
    COUPON_ALREADY_ISSUED(HttpStatus.BAD_REQUEST, "이미 발급된 쿠폰입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}