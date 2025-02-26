package kr.ontherec.api.domain.item.exception;

import kr.ontherec.api.global.exception.CustomException;
import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class ItemException extends CustomException {
    public ItemException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
