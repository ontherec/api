package kr.ontherec.api.modules.practiceroom.exception;

import kr.ontherec.api.infra.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum PracticeRoomExceptionCode implements ExceptionCode {
    EXIST_BRN("AP001", HttpStatus.BAD_REQUEST, "이미 존재하는 사업자등록번호 입니다."),
    NOT_VALID_BOOKING_PERIOD("AP002", HttpStatus.BAD_REQUEST, "예약 기간은 최소 7일 이상이어야 합니다."),
    NOT_VALID_PARKING("AP003", HttpStatus.BAD_REQUEST, "주차가 가능한 경우에 대해서만 요금 여부를 설정할 수 있습니다."),
    NOT_VALID_FILTER("AP005", HttpStatus.BAD_REQUEST, "유효하지 않은 필터입니다."),
    NOT_SUPPORT_FILTER("AP006", HttpStatus.BAD_REQUEST, "지원하지 않는 필터입니다."),
    NOT_FOUND("AP007", HttpStatus.NOT_FOUND, "요청하신 연습실을 찾을 수 없습니다."),
    FORBIDDEN("AP008", HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
