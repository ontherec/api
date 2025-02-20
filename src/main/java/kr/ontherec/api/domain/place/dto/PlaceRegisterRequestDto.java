package kr.ontherec.api.domain.place.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.ontherec.api.domain.place.domain.HolidayType;
import kr.ontherec.api.domain.place.domain.RefundPolicy;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.time.DurationMax;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public record PlaceRegisterRequestDto(
        @NotBlank(message = "사업자 등록 번호를 입력해주세요")
        String brn,
        @NotBlank(message = "공간 이름을 입력해주세요")
        String name,
        @NotNull(message = "주소를 입력해주세요")
        AddressRegisterRequestDto address,
        String introduction,
        List<@Size(max = 10, message = "키워드는 최대 10글자 입니다") String> keywords,
        List<@URL(message = "유효하지 않은 URL 입니다") String> links,
        Set<RefundPolicy> refundPolicies,
        @DurationMax(days = 90L, message = "예약 기간은 최대 90일 전까지 설정 가능합니다")
        Duration bookingFrom,
        Duration bookingUntil,
        List<HolidayType> holidays
) {
    /**
     * <a href="https://namu.wiki/w/대한민국/행정구역">나무위키: 행정구역</a>
     */
    private record AddressRegisterRequestDto(
            @NotNull(message = "우편번호를 입력해주세요")
            @Size(min = 5, max = 5, message = "유효하지 않은 우편번호 입니다")
            int zipcode,
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
}
