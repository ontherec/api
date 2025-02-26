package kr.ontherec.api.domain.tag.exception;

import kr.ontherec.api.global.exception.CustomException;
import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class TagException extends CustomException {
    public TagException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
