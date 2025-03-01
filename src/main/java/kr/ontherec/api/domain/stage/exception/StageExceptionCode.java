package kr.ontherec.api.domain.stage.exception;

import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum StageExceptionCode implements ExceptionCode {
    NOT_FOUND("AS001", HttpStatus.NOT_FOUND, "요청하신 공연장을 찾을 수 없습니다."),
    NOT_VALID_ENGINEERING_FEE("AS002", HttpStatus.BAD_REQUEST, "제공되는 엔지니어링에 대해서만 요금을 설정할 수 있습니다."),
    NOT_VALID_PARKING("AS003", HttpStatus.BAD_REQUEST, "주차가 가능한 경우에 대해서만 요금 여부를 설정할 수 있습니다."),
    FORBIDDEN("AS004", HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
