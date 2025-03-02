package kr.ontherec.api.domain.host.exception;

import kr.ontherec.api.global.exception.CustomException;
import lombok.Getter;

@Getter
public class HostException extends CustomException {
    public HostException(HostExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
