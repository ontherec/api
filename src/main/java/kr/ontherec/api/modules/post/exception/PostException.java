package kr.ontherec.api.modules.post.exception;

import kr.ontherec.api.infra.exception.CustomException;
import lombok.Getter;

@Getter
public class PostException extends CustomException {
    public PostException(PostExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
