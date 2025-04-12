package kr.ontherec.api.modules.practiceroom.dto;

import kr.ontherec.api.modules.host.dto.HostResponseDto;
import kr.ontherec.api.modules.item.entity.Address;
import kr.ontherec.api.modules.item.entity.HolidayType;
import kr.ontherec.api.modules.item.entity.RefundPolicy;
import kr.ontherec.api.modules.practiceroom.entity.PracticeRoomTimeBlock;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PracticeRoomResponseDto {
    Long id;
    HostResponseDto host;
    List<String> images;
    String title;
    String brn;
    Address address;
    long viewCount;
    long likeCount;
    boolean liked;
    Introduction introduction;
    Area area;
    Business business;
    Parking parking;
    Facilities facilities;
    FnbPolicies fnbPolicies;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;

    public record Introduction (
            String content,
            Set<String> tags,
            Set<String> links
    ) {}

    public record Area (
            int standardCapacity,
            int maxCapacity,
            BigDecimal extraPerPerson
    ) {}

    public record Business (
            Set<HolidayType> holidays,
            Set<PracticeRoomTimeBlock> timeBlocks,
            Duration bookingFrom,
            Duration bookingUntil,
            Set<RefundPolicy> refundPolicies
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
            boolean hasCameraStanding
    ) {}

    public record FnbPolicies (
            boolean allowsWater,
            boolean allowsDrink,
            boolean allowsFood,
            boolean sellDrink
    ) {}
}