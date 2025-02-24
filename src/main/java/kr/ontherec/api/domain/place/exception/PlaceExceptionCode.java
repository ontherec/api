package kr.ontherec.api.domain.place.exception;

import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PlaceExceptionCode implements ExceptionCode {
    NOT_VALID_REFUND_POLICY("AP001", HttpStatus.BAD_REQUEST, "유효하지 않은 환불 정책입니다."),
    NOT_VALID_BOOKING_PERIOD("AP002", HttpStatus.BAD_REQUEST, "예약 기간은 최소 7일 이상이어야 합니다."),
    EXIST_BRN("AP003", HttpStatus.BAD_REQUEST, "이미 존재하는 사업자등록번호 입니다."),
    NOT_FOUND("AP004", HttpStatus.NOT_FOUND, "요청하신 플레이스를 찾을 수 없습니다."),
    FORBIDDEN("AP005", HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
