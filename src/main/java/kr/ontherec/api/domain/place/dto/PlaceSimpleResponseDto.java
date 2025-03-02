package kr.ontherec.api.domain.place.dto;

import kr.ontherec.api.domain.place.domain.Address;

import java.util.Set;

public record PlaceSimpleResponseDto(
        Long id,
        String title,
        Address address,
        Set<String> tags
) { }
