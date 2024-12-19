package kr.ontherec.backend.global.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommonExceptionCode implements ExceptionCode {
    // 400 Bad Request
    NOT_VALID(HttpStatus.BAD_REQUEST, "유효하지 않은 입력입니다."),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "기대하는 타입과 일치하지 않습니다."),
	MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),

    // 401 Unauthorized
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "유효한 자격 증명이 없습니다."),

    // 403 Forbidden
	FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 404 Not Found
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 경로를 찾을 수 없습니다."),

    // 415 UNSUPPORTED_MEDIA_TYPE
	UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 형식입니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus status() {
        return this.status;
    }

    @Override
    public String message() {
        return this.message;
    }
}
