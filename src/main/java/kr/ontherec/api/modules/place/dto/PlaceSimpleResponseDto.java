package kr.ontherec.api.modules.place.dto;

import kr.ontherec.api.modules.place.entity.Address;

import java.util.Set;

public record PlaceSimpleResponseDto(
        Long id,
        Address address,
        String title,
        Set<String> tags
) { }
