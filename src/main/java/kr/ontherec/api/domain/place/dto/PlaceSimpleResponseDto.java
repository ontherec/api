package kr.ontherec.api.domain.place.dto;

import kr.ontherec.api.domain.place.domain.Address;

import java.util.Set;

public record PlaceSimpleResponseDto(
        Long id,
        Address address,
        String title,
        Set<String> tags
) { }
