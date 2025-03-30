package kr.ontherec.api.modules.host.exception;

import kr.ontherec.api.infra.exception.CustomException;
import lombok.Getter;

@Getter
public class HostException extends CustomException {
    public HostException(HostExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
