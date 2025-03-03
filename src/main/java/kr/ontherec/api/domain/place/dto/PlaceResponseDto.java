package kr.ontherec.api.domain.place.dto;

import kr.ontherec.api.domain.host.dto.HostResponseDto;
import kr.ontherec.api.domain.place.domain.Address;
import kr.ontherec.api.domain.place.domain.HolidayType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

public record PlaceResponseDto(
        Long id,
        HostResponseDto host,
        String brn,
        Address address,
        Introduction introduction,
        Business business,
        Parking parking,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public record Introduction (
            String title,
            String content,
            Set<String> tags,
            Set<String> links
    ) {}

    public record Business (
            Duration bookingFrom,
            Duration bookingUntil,
            Set<HolidayType> holidays
    ) {}

    public record Parking (
            int capacity,
            String location,
            Boolean free
    ) {}
}
