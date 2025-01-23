package kr.ontherec.api.global.model;

import org.springframework.http.HttpStatus;

public interface SuccessCode {
	HttpStatus status();

	String message();
}
