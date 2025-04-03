package kr.ontherec.api.modules.stage.dto;

import kr.ontherec.api.modules.host.dto.HostResponseDto;
import kr.ontherec.api.modules.item.entity.Address;
import kr.ontherec.api.modules.item.entity.HolidayType;
import kr.ontherec.api.modules.item.entity.RefundPolicy;
import kr.ontherec.api.modules.stage.entity.StageType;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record StageResponseDto (
        Long id,
        HostResponseDto host,
        List<String> images,
        String title,
        String brn,
        Address address,
        long viewCount,
        long likeCount,
        Introduction introduction,
        Area area,
        Business business,
        Engineering engineering,
        Documents documents,
        Parking parking,
        Facilities facilities,
        FnbPolicies fnbPolicies,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public record Introduction (
            String content,
            Set<String> tags,
            Set<String> links
    ) {}

    public record Area (
            int minCapacity,
            int maxCapacity,
            StageType stageType,
            BigDecimal stageWidth,
            BigDecimal stageHeight
    ) {}

    public record Business (
            Duration bookingFrom,
            Duration bookingUntil,
            Set<HolidayType> holidays,
            Set<RefundPolicy> refundPolicies
    ) {}

    public record Engineering (
            boolean stageManagingAvailable,
            Long stageManagingFee,
            boolean soundEngineeringAvailable,
            Long soundEngineeringFee,
            boolean lightEngineeringAvailable,
            Long lightEngineeringFee,
            boolean photographingAvailable,
            Long photographingFee
    ) {}

    public record Documents (
            String applicationForm,
            String cueSheetTemplate,
            Duration cueSheetDue
    ) {}

    public record Parking (
            int capacity,
            String location,
            Boolean free
    ) {}

    public record Facilities (
            boolean hasElevator,
            boolean hasRestroom,
            boolean hasWifi,
            boolean hasCameraStanding,
            boolean hasWaitingRoom,
            boolean hasProjector,
            boolean hasLocker
    ) {}

    public record FnbPolicies (
            boolean allowsWater,
            boolean allowsDrink,
            boolean allowsFood,
            boolean allowsFoodDelivery,
            boolean allowsAlcohol,
            boolean sellDrink,
            boolean sellAlcohol
    ) {}
}