package kr.ontherec.api.modules.chat.exception;

import kr.ontherec.api.infra.exception.CustomException;
import lombok.Getter;

@Getter
public class ChatException extends CustomException {
    public ChatException(ChatExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
