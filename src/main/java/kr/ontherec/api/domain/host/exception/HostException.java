package kr.ontherec.api.domain.host.exception;

import kr.ontherec.api.global.exception.CustomException;
import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class HostException extends CustomException {

    public HostException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
