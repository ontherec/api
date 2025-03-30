package kr.ontherec.api.modules.host.exception;

import kr.ontherec.api.infra.exception.ExceptionCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum HostExceptionCode implements ExceptionCode {
    EXIST_USERNAME("AH001", HttpStatus.BAD_REQUEST, "이미 등록된 호스트 입니다."),
    NOT_FOUND("AH002", HttpStatus.NOT_FOUND, "요청하신 호스트를 찾을 수 없습니다."),
    NOT_VALID_CONTACT_TIME("AH003", HttpStatus.BAD_REQUEST, "연락 가능 시간은 최소 30분이어야 합니다.");
  
    private final String code;
    private final HttpStatus status;
    private final String message;
}
