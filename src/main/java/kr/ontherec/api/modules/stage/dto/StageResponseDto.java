package kr.ontherec.api.modules.stage.dto;

import kr.ontherec.api.modules.item.entity.RefundPolicy;
import kr.ontherec.api.modules.place.dto.PlaceSimpleResponseDto;
import kr.ontherec.api.modules.stage.entity.StageType;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

public record StageResponseDto (
        Long id,
        PlaceSimpleResponseDto place,
        Introduction introduction,
        long viewCount,
        long likeCount,
        Location location,
        Area area,
        Business business,
        Engineering engineering,
        Documents documents,
        Facilities facilities,
        FnbPolicies fnbPolicies,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public record Introduction (
            String title,
            String content,
            String guide,
            Set<String> tags
    ) {}

    public record Location (
            int floor,
            boolean hasElevator
    ) {}

    public record Area (
            int minCapacity,
            int maxCapacity,
            StageType stageType,
            BigDecimal stageWidth,
            BigDecimal stageHeight
    ) {}

    public record Business (
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

    public record Facilities (
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