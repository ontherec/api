package kr.ontherec.api.modules.host.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import kr.ontherec.api.modules.host.entity.Bank;

import static kr.ontherec.api.infra.model.Regex.BANK_ACCOUNT;

public record HostRegisterRequestDto(
        @NotNull(message = "은행을 입력해주세요")
        Bank bank,
        @NotBlank(message = "계좌번호를 입력해주세요")
        @Pattern(regexp = BANK_ACCOUNT, message = "유효하지 않은 계좌번호 입니다")
        String account
) {
}
