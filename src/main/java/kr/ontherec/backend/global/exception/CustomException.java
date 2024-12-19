package kr.ontherec.backend.global.exception;

import kr.ontherec.backend.global.code.ExceptionCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.message());
        this.exceptionCode = exceptionCode;
    }

    public int getHttpStatus() {
        return exceptionCode.status().value();
    }
}
