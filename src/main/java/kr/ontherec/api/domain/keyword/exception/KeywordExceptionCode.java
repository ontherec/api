package kr.ontherec.api.domain.keyword.exception;

import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum KeywordExceptionCode implements ExceptionCode {
    NOT_FOUND("AK001", HttpStatus.NOT_FOUND, "요청하신 키워드를 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
