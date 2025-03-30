package kr.ontherec.api.infra.s3.exception;


import kr.ontherec.api.infra.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum S3ExceptionCode implements ExceptionCode {
    NOT_VALID_FILE_EXTENSION("AS001", HttpStatus.BAD_REQUEST, "유효하지 않은 파일 확장자입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
