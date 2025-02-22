package kr.ontherec.api.domain.keyword.exception;

import kr.ontherec.api.global.exception.CustomException;
import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class KeywordException extends CustomException {
    public KeywordException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
