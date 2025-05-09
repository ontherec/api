package kr.ontherec.api.modules.item.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Duration;

public record RefundPolicyRegisterRequestDto(
        @NotNull(message = "환불 마감 기한을 입력해주세요.")
        Duration dayBefore,
        @NotNull(message = "환불 비율을 입력해주세요.")
        @DecimalMin(value = "0", message = "환불 수수료 비율은 최소 0까지 설정 가능합니다.")
        @DecimalMax(value = "100", message = "환불 수수료 비율은 최대 100까지 설정 가능합니다.")
        @Digits(integer = 3, fraction = 1, message = "환불 수수료 비율은 최대 소수점 첫 번째 자리까지 설정 가능합니다.")
        BigDecimal percent
) { }
