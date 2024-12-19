package kr.ontherec.backend.global.code;

import org.springframework.http.HttpStatus;

public interface SuccessCode {
    HttpStatus status();
    String message();
}
