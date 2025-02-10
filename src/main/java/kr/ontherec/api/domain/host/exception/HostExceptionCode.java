package kr.ontherec.api.domain.host.exception;

import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum HostExceptionCode implements ExceptionCode {
    EXIST_USERNAME("AH001", HttpStatus.BAD_REQUEST, "이미 등록된 호스트 입니다."),
    NOT_FOUND("AH002", HttpStatus.NOT_FOUND, "요청하신 호스트를 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
