package kr.ontherec.api.domain.place.dto;

import jakarta.validation.constraints.*;
import kr.ontherec.api.domain.place.domain.HolidayType;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.time.DurationMax;

import java.time.Duration;
import java.util.Set;

public class PlaceUpdateRequestDto {
        public record Introduction (
                @NotBlank(message = "플레이스 이름을 입력해주세요.")
                String title,
                String content,
                Set<@Size(max = 10, message = "태그는 최대 10글자 입니다.") String> tags,
                Set<@URL(message = "유효하지 않은 URL 입니다.") String> links
        ) {}

        public record Business (
                @NotNull(message = "예약 시작 기간을 입력해주세요")
                @DurationMax(days = 90L, message = "예약 기간은 최대 90일 전까지 설정 가능합니다")
                Duration bookingFrom,
                @NotNull(message = "예약 마감 기간을 입력해주세요")
                Duration bookingUntil,
                Set<HolidayType> holidays
        ) {}

        public record Parking (
                @NotNull(message = "주차대수를 입력해주세요.")
                @DecimalMax(value = "100", message = "주차대수는 최대 100대까지 설정 가능합니다.")
                @PositiveOrZero(message = "주차대수는 최소 0대까지 설정 가능합니다.")
                int capacity,
                @Size(max = 50, message = "주차장 위치 정보는 최대 50글자까지 작성할 수 있습니다.")
                String location,
                Boolean free
        ) {}
}
