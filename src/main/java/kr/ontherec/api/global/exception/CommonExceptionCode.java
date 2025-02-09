package kr.ontherec.api.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommonExceptionCode implements ExceptionCode {
    NOT_VALID("AC001", HttpStatus.BAD_REQUEST, "유효하지 않은 입력입니다."),
    TYPE_MISMATCH("AC002", HttpStatus.BAD_REQUEST, "기대하는 타입과 일치하지 않습니다."),
    MISSING_PARAMETER("AC003", HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    UNAUTHORIZED("AC004", HttpStatus.UNAUTHORIZED, "유효한 자격 증명이 없습니다."),
    FORBIDDEN("AC005", HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다."),
    NOT_FOUND("AC006", HttpStatus.NOT_FOUND, "요청하신 경로를 찾을 수 없습니다."),
    UNSUPPORTED_MEDIA_TYPE("AC007", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 형식입니다."),
    INTERNAL_SERVER_ERROR("AC008", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
