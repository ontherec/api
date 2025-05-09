package kr.ontherec.api.modules.stage.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import kr.ontherec.api.modules.item.dto.AddressRegisterRequestDto;
import kr.ontherec.api.modules.item.dto.RefundPolicyRegisterRequestDto;
import kr.ontherec.api.modules.item.entity.HolidayType;
import kr.ontherec.api.modules.stage.entity.StageType;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.time.DurationMax;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import static kr.ontherec.api.infra.entity.Regex.BUSINESS_REGISTRATION_NUMBER;


public record StageRegisterRequestDto (
        @NotNull(message = "공연장 사진을 입력해주세요.")
        List<@URL(message = "유효하지 않은 URL 입니다") String> images,
        @NotBlank(message = "공연장 이름을 입력해주세요.")
        String title,
        @NotBlank(message = "사업자 등록 번호를 입력해주세요")
        @Pattern(regexp = BUSINESS_REGISTRATION_NUMBER, message = "유효하지 않은 사업자등록번호입니다")
        String brn,
        @Valid
        @NotNull(message = "주소를 입력해주세요")
        AddressRegisterRequestDto address,
        @Valid @NotNull(message = "공연장 소개를 입력해주세요.")
        Introduction introduction,
        @Valid @NotNull(message = "공연장 면적 정보를 입력해주세요.")
        Area area,
        @Valid @NotNull(message = "공연장 영업 정보를 입력해주세요.")
        Business business,
        @Valid @NotNull(message = "공연장 엔지니어링 정보를 입력해주세요.")
        Engineering engineering,
        @Valid @NotNull(message = "공연장 문서 정보를 입력해주세요.")
        Documents documents,
        @Valid @NotNull(message = "공연장 주차 정보를 입력해주세요.")
        Parking parking,
        @Valid @NotNull(message = "공연장 편의시설 정보를 입력해주세요.")
        Facilities facilities,
        @Valid @NotNull(message = "공연장 식음료 정책을 입력해주세요.")
        FnbPolicies fnbPolicies
) {
        public record Introduction (
                @NotBlank(message = "공연장 소개를 입력해주세요.")
                @Size(max = 1000, message = "공연장 소개는 최대 1000글자까지 입력 가능합니다.")
                String content,
                Set<@Size(max = 10, message = "태그는 최대 10글자 입니다") String> tags,
                Set<@URL(message = "유효하지 않은 URL 입니다") String> links
        ) {}

        public record Area (
                @NotNull(message = "최소 수용인원(좌석 기준)을 입력해주세요.")
                @DecimalMax(value = "1000", message = "수용인원은 최대 천명까지 설정 가능합니다.")
                @Positive(message = "수용인원은 최소 1명까지 설정 가능합니다.")
                int minCapacity,
                @NotNull(message = "최대 수용인원(스탠딩 기준)을 입력해주세요.")
                @DecimalMax(value = "1000", message = "수용인원은 최대 천명까지 설정 가능합니다.")
                @Positive(message = "수용인원은 최소 1명까지 설정 가능합니다.")
                int maxCapacity,
                @NotNull(message = "무대 타입을 입력해주세요.")
                StageType stageType,
                @NotNull(message = "무대 가로 길이를 입력해주세요.")
                @DecimalMax(value = "1000", message = "무대 길이는 최대 1000m까지 설정 가능합니다.")
                @Digits(integer = 4, fraction = 1, message = "무대 길이는 소수점 첫 번째 자리까지 입력 가능합니다.")
                BigDecimal stageWidth,
                @NotNull(message = "무대 세로 길이를 입력해주세요.")
                @DecimalMax(value = "1000", message = "무대 길이는 최대 1000m까지 설정 가능합니다.")
                @Digits(integer = 4, fraction = 1, message = "무대 길이는 소수점 첫 번째 자리까지 입력 가능합니다.")
                BigDecimal stageHeight
        ) {}

        public record Business (
                Set<HolidayType> holidays,
                @NotNull(message = "영업 시간을 입력해주세요.")
                Set<@Valid StageTimeBlockCreateRequestDto> timeBlocks,
                @NotNull(message = "예약 시작 기간을 입력해주세요")
                @DurationMax(days = 90L, message = "예약 기간은 최대 90일 전까지 설정 가능합니다")
                Duration bookingFrom,
                @DurationMax(days = 90L, message = "예약 기간은 최대 90일 전까지 설정 가능합니다")
                @NotNull(message = "예약 마감 기간을 입력해주세요")
                Duration bookingUntil,
                @NotNull(message = "환불 정책을 입력해주세요.")
                Set<@Valid RefundPolicyRegisterRequestDto> refundPolicies
        ) {}

        public record Engineering (
                @NotNull(message = "스테이지 매니징 제공 여부를 입력해주세요.")
                boolean stageManagingAvailable,
                Long stageManagingFee,
                @NotNull(message = "사운드 엔지니어링 제공 여부를 입력해주세요.")
                boolean soundEngineeringAvailable,
                Long soundEngineeringFee,
                @NotNull(message = "조명 엔지니어링 제공 여부를 입력해주세요.")
                boolean lightEngineeringAvailable,
                Long lightEngineeringFee,
                @NotNull(message = "촬영 제공 여부를 입력해주세요.")
                boolean photographingAvailable,
                Long photographingFee
        ) {}

        public record Documents (
                @URL(message = "유효하지 않은 URL 입니다.")
                String applicationForm,
                @NotNull(message = "큐시트 템플릿 양식을 입력해주세요.")
                @URL(message = "유효하지 않은 URL 입니다.")
                String cueSheetTemplate,
                @NotNull(message = "큐시트 제출 마감 기한을 입력해주세요.")
                Duration cueSheetDue
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
                boolean hasCameraStanding,
                @NotNull(message = "대기실 존재 여부를 입력해주세요.")
                boolean hasWaitingRoom,
                @NotNull(message = "프로젝터 존재 여부를 입력해주세요.")
                boolean hasProjector,
                @NotNull(message = "물품보관함 존재 여부를 입력해주세요.")
                boolean hasLocker
        ) {}

        public record FnbPolicies (
                @NotNull(message = "물 반입 허용 여부를 입력해주세요.")
                boolean allowsWater,
                @NotNull(message = "음료 반입 허용 여부를 입력해주세요.")
                boolean allowsDrink,
                @NotNull(message = "음식 반입 허용 여부를 입력해주세요.")
                boolean allowsFood,
                @NotNull(message = "음식 배달 허용 여부를 입력해주세요.")
                boolean allowsFoodDelivery,
                @NotNull(message = "주류 반입 허용 여부를 입력해주세요.")
                boolean allowsAlcohol,
                @NotNull(message = "음료 판매 여부를 입력해주세요.")
                boolean sellDrink,
                @NotNull(message = "주류 판매 여부를 입력해주세요.")
                boolean sellAlcohol
        ) {}
}