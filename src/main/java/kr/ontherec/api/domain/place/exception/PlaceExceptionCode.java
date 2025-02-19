package kr.ontherec.api.domain.place.exception;

import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PlaceExceptionCode implements ExceptionCode {
    NOT_VALID_REFUND_POLICY("AP001", HttpStatus.BAD_REQUEST, "유효하지 않은 환불 정책입니다"),
    NOT_VALID_BOOKING_PERIOD("AP003", HttpStatus.BAD_REQUEST, "예약 가능 기간은 최대 90일 전까지 설정할 수 있습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
