package kr.ontherec.api.modules.tag.exception;

import kr.ontherec.api.infra.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum TagExceptionCode implements ExceptionCode {
    NOT_FOUND("AK001", HttpStatus.NOT_FOUND, "요청하신 태그를 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
