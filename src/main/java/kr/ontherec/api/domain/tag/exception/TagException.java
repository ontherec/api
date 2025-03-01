package kr.ontherec.api.domain.tag.exception;

import kr.ontherec.api.global.exception.CustomException;
import lombok.Getter;

@Getter
public class TagException extends CustomException {
    public TagException(TagExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
