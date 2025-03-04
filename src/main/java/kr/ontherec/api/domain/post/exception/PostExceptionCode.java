package kr.ontherec.api.domain.post.exception;


import kr.ontherec.api.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum PostExceptionCode implements ExceptionCode {
    NOT_FOUND("AP001", HttpStatus.NOT_FOUND, "요청하신 게시글을 찾을 수 없습니다."),
    FORBIDDEN("AP002", HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
