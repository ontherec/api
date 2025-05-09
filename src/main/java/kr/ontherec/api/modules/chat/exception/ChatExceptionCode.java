package kr.ontherec.api.modules.chat.exception;

import kr.ontherec.api.infra.exception.ExceptionCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ChatExceptionCode implements ExceptionCode {
    NOT_FOUND("AC001", HttpStatus.FORBIDDEN, "요청하신 채팅방을 찾을 수 없습니다."),
    FORBIDDEN("AC002", HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다."),
    NOT_FOUND_PARTICIPANT("AC003", HttpStatus.FORBIDDEN, "요청하신 참여자를 찾을 수 없습니다."),
    NOT_FOUND_MESSAGE("AC004", HttpStatus.FORBIDDEN, "요청하신 메시지를 찾을 수 없습니다."),
    NOT_VALID_MESSAGE("AC005", HttpStatus.BAD_REQUEST, "유효하지 않은 메시지입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
