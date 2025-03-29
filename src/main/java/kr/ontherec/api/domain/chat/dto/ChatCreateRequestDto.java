package kr.ontherec.api.domain.chat.dto;

import java.util.Set;

public record ChatCreateRequestDto (
        String title,
        Set<String> participants
) { }
