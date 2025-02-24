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
        String name,
        Address address,
        String introduction,
        Set<String> keywords,
        Set<String> links,
        Duration bookingFrom,
        Duration bookingUntil,
        Set<HolidayType> holidays,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) { }
