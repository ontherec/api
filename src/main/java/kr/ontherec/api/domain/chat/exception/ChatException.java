package kr.ontherec.api.domain.chat.exception;

import kr.ontherec.api.global.exception.CustomException;
import lombok.Getter;

@Getter
public class ChatException extends CustomException {
    public ChatException(ChatExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
