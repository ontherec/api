package kr.ontherec.api.domain.place.exception;

import kr.ontherec.api.global.exception.CustomException;
import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class PlaceException extends CustomException {
    public PlaceException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
