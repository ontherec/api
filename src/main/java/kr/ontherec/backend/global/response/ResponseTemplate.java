package kr.ontherec.backend.global.response;

import kr.ontherec.backend.global.code.ExceptionCode;
import kr.ontherec.backend.global.code.SuccessCode;

public record ResponseTemplate(boolean success, String message, Object data) {

	public static ResponseTemplate success(SuccessCode type) {
		return new ResponseTemplate(true, type.message(), null);
	}

	public static ResponseTemplate success(SuccessCode type, Object data) {
		return new ResponseTemplate(true, type.message(), data);
	}

	public static ResponseTemplate error(ExceptionCode type) {
		return new ResponseTemplate(false, type.message(), null);
	}
}
