package kr.ontherec.api.modules.item.exception;

import kr.ontherec.api.infra.exception.CustomException;
import lombok.Getter;

@Getter
public class TagException extends CustomException {
    public TagException(TagExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
