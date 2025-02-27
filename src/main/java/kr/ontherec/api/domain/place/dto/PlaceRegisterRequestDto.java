package kr.ontherec.api.domain.place.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kr.ontherec.api.domain.place.domain.HolidayType;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.time.DurationMax;

import java.time.Duration;
import java.util.Set;

import static kr.ontherec.api.global.model.Regex.BUSINESS_REGISTRATION_NUMBER;

public record PlaceRegisterRequestDto(
        @NotBlank(message = "사업자 등록 번호를 입력해주세요")
        @Pattern(regexp = BUSINESS_REGISTRATION_NUMBER, message = "유효하지 않은 사업자 등록번호입니다")
        String brn,
        @NotBlank(message = "플레이스 제목을 입력해주세요")
        String title,
        @Valid
        @NotNull(message = "주소를 입력해주세요")
        AddressRegisterRequestDto address,
        @Size(max = 1000, message = "공연장 소개는 최대 1000글자까지 입력 가능합니다.")
        String introduction,
        Set<@Size(max = 10, message = "태그는 최대 10글자 입니다") String> tags,
        Set<@URL(message = "유효하지 않은 URL 입니다") String> links,
        @NotNull(message = "예약 시작 기간을 입력해주세요")
        @DurationMax(days = 90L, message = "예약 기간은 최대 90일 전까지 설정 가능합니다")
        Duration bookingFrom,
        @NotNull(message = "예약 마감 기간을 입력해주세요")
        Duration bookingUntil,
        Set<HolidayType> holidays
) { }