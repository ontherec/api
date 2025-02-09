package kr.ontherec.api.global.model;

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
