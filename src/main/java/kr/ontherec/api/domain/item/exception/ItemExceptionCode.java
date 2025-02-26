package kr.ontherec.api.domain.item.exception;

import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum ItemExceptionCode implements ExceptionCode {
    NOT_VALID_REFUND_POLICY("AI001", HttpStatus.BAD_REQUEST, "유효하지 않은 환불 정책입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
