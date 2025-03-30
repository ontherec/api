package kr.ontherec.api.modules.stage.exception;

import kr.ontherec.api.infra.exception.CustomException;
import lombok.Getter;

@Getter
public class StageException extends CustomException {
    public StageException(StageExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
