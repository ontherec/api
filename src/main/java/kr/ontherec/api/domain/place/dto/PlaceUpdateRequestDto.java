package kr.ontherec.api.domain.place.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.ontherec.api.domain.place.domain.HolidayType;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.time.DurationMax;

import java.time.Duration;
import java.util.Set;

public record PlaceUpdateRequestDto(
        @NotBlank(message = "공간 이름을 입력해주세요")
        String name,
        String introduction,
        Set<@Size(max = 10, message = "태그는 최대 10글자 입니다") String> tags,
        Set<@URL(message = "유효하지 않은 URL 입니다") String> links,
        @DurationMax(days = 90L, message = "예약 기간은 최대 90일 전까지 설정 가능합니다")
        Duration bookingFrom,
        Duration bookingUntil,
        Set<HolidayType> holidays
) { }
