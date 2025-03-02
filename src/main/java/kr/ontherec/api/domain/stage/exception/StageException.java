package kr.ontherec.api.domain.stage.exception;

import kr.ontherec.api.global.exception.CustomException;
import lombok.Getter;

@Getter
public class StageException extends CustomException {
    public StageException(StageExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
