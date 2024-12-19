package kr.ontherec.backend.global.code;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {
    HttpStatus status();
    String message();
}
