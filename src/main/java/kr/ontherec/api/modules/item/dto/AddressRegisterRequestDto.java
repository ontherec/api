package kr.ontherec.api.modules.item.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

import static kr.ontherec.api.infra.model.Regex.ZIP_CODE;

/**
 * <a href="https://namu.wiki/w/대한민국/행정구역">나무위키: 행정구역</a>
 */
public record AddressRegisterRequestDto(
        @NotNull(message = "우편번호를 입력해주세요")
        @Pattern(regexp = ZIP_CODE, message = "유효하지 않은 우편번호입니다")
        String zipcode,
        @NotBlank(message = "도/시 정보를 입력해주세요")
        String state,
        @NotBlank(message = "시/군/구 정보를 입력해주세요")
        String city,
        @NotBlank(message = "도로명 주소를 입력해주세요")
        String streetAddress,
        String detail,
        @NotNull(message = "위도를 입력해주세요")
        @Digits(integer = 3, fraction = 13, message = "유효하지 않은 위도입니다")
        BigDecimal latitude,
        @NotNull(message = "경도를 입력해주세요")
        @Digits(integer = 3, fraction = 13, message = "유효하지 않은 경도입니다")
        BigDecimal longitude
) { }