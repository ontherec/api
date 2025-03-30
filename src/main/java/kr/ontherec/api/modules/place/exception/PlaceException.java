package kr.ontherec.api.modules.place.exception;

import kr.ontherec.api.infra.exception.CustomException;
import lombok.Getter;

@Getter
public class PlaceException extends CustomException {
    public PlaceException(PlaceExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
