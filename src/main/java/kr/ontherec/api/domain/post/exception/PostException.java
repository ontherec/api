package kr.ontherec.api.domain.post.exception;

import kr.ontherec.api.global.exception.CustomException;
import lombok.Getter;

@Getter
public class PostException extends CustomException {
    public PostException(PostExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
