package kr.ontherec.api.domain.stage.dto;

import kr.ontherec.api.domain.item.domain.RefundPolicy;
import kr.ontherec.api.domain.place.dto.PlaceSimpleResponseDto;
import kr.ontherec.api.domain.stage.domain.StageType;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

public record StageResponseDto (
        Long id,
        PlaceSimpleResponseDto place,

        // location
        String title,
        int floor,
        boolean hasElevator,

        // introduction
        String introduction,
        String guide,
        Set<String> tags,

        // count
        long viewCount,
        long likeCount,

        // Area
        int minCapacity,
        int maxCapacity,
        StageType stageType,
        BigDecimal stageWidth,
        BigDecimal stageHeight,

        // business
        Set<RefundPolicy> refundPolicies,

        // engineering
        boolean stageManagingAvailable,
        Long stageManagingFee,
        boolean soundEngineeringAvailable,
        Long soundEngineeringFee,
        boolean lightEngineeringAvailable,
        Long lightEngineeringFee,
        boolean photographingAvailable,
        Long photographingFee,

        // documents
        String applicationForm,
        String cueSheetTemplate,
        Duration cueSheetDue,

        // facilities
        boolean hasRestroom,
        boolean hasWifi,
        boolean hasCameraStanding,
        boolean hasWaitingRoom,
        boolean hasProjector,
        boolean hasLocker,

        // fnb policies
        boolean allowsWater,
        boolean allowsDrink,
        boolean allowsFood,
        boolean allowsFoodDelivery,
        boolean allowsAlcohol,
        boolean sellDrink,
        boolean sellAlcohol,

        // time
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) { }