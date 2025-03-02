package kr.ontherec.api.domain.place.exception;

import kr.ontherec.api.global.exception.CustomException;
import lombok.Getter;

@Getter
public class PlaceException extends CustomException {
    public PlaceException(PlaceExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
