package kr.ontherec.api.domain.stage.exception;

import kr.ontherec.api.global.exception.CustomException;
import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class StageException extends CustomException {
    public StageException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
