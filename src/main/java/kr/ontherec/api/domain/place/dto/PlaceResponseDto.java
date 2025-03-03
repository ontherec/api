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
        // location
        String title,
        Address address,
        // introduction
        String introduction,
        Set<String> tags,
        Set<String> links,
        // business
        Duration bookingFrom,
        Duration bookingUntil,
        Set<HolidayType> holidays,
        // parking
        int parkingCapacity,
        String parkingLocation,
        Boolean freeParking,
        // time
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) { }
