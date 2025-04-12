package kr.ontherec.api.modules.practiceroom.exception;

import kr.ontherec.api.infra.exception.CustomException;
import lombok.Getter;

@Getter
public class PracticeRoomException extends CustomException {
    public PracticeRoomException(PracticeRoomExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
