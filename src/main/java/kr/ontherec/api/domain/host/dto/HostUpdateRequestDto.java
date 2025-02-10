package kr.ontherec.api.domain.host.dto;

import jakarta.validation.constraints.Pattern;
import kr.ontherec.api.domain.host.domain.Bank;

import java.time.LocalTime;

public record HostUpdateRequestDto(
        Bank bank,
        @Pattern(regexp = "d{8,14}", message = "유효하지 않은 계좌번호 입니다")
        String account,
        LocalTime contactFrom,
        LocalTime contactUntil
) {
}
