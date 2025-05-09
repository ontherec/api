package kr.ontherec.api.modules.practiceroom.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import kr.ontherec.api.modules.item.dto.RefundPolicyRegisterRequestDto;
import kr.ontherec.api.modules.item.entity.HolidayType;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.time.DurationMax;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class PracticeRoomUpdateRequestDto {
    public record Images(
            @NotNull(message = "공연장 사진을 입력해주세요.")
            List<@URL(message = "유효하지 않은 URL 입니다") String> images
    ) {}

    public record Introduction (
            @NotBlank(message = "연습실 소개를 입력해주세요.")
            @Size(max = 1000, message = "연습실 소개는 최대 1000글자까지 입력 가능합니다.")
            String content,
            Set<@Size(max = 10, message = "태그는 최대 10글자 입니다") String> tags,
            Set<@URL(message = "유효하지 않은 URL 입니다") String> links
    ) {}

    public record Area (
            @NotNull(message = "기준 이용인원을 입력해주세요.")
            @DecimalMax(value = "100", message = "기존 이용인원은 최대 100명까지 설정 가능합니다.")
            @Positive(message = "이용인원은 최소 1명까지 설정 가능합니다.")
            int standardCapacity,
            @NotNull(message = "최대 이용인원을 입력해주세요.")
            @DecimalMax(value = "100", message = "기존 이용인원은 최대 100명까지 설정 가능합니다.")
            @Positive(message = "이용인원은 최소 1명까지 설정 가능합니다.")
            int maxCapacity,
            @NotNull(message = "초과인원당 추가 비용을 입력해주세요")
            long extraPerPerson
    ) {}

    public record Business (
            Set<HolidayType> holidays,
            @NotNull(message = "영업 시간을 입력해주세요.")
            Set<@Valid PracticeRoomTimeBlockCreateRequestDto> timeBlocks,
            @NotNull(message = "예약 시작 기간을 입력해주세요")
            @DurationMax(days = 90L, message = "예약 기간은 최대 90일 전까지 설정 가능합니다")
            Duration bookingFrom,
            @DurationMax(days = 90L, message = "예약 기간은 최대 90일 전까지 설정 가능합니다")
            @NotNull(message = "예약 마감 기간을 입력해주세요")
            Duration bookingUntil,
            @NotNull(message = "환불 정책을 입력해주세요.")
            Set<@Valid RefundPolicyRegisterRequestDto> refundPolicies
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

    public record Facilities (
            @NotNull(message = "엘리베이터 존재 여부를 입력해주세요.")
            boolean hasElevator,
            @NotNull(message = "화장실 존재 여부를 입력해주세요.")
            boolean hasRestroom,
            @NotNull(message = "와이파이 제공 여부를 입력해주세요.")
            boolean hasWifi,
            @NotNull(message = "카메라 스탠드 제공 여부를 입력해주세요.")
            boolean hasCameraStanding
    ) {}

    public record FnbPolicies (
            @NotNull(message = "물 반입 허용 여부를 입력해주세요.")
            boolean allowsWater,
            @NotNull(message = "음료 반입 허용 여부를 입력해주세요.")
            boolean allowsDrink,
            @NotNull(message = "음식 반입 허용 여부를 입력해주세요.")
            boolean allowsFood,
            @NotNull(message = "음료 판매 여부를 입력해주세요.")
            boolean sellDrink
    ) {}
}