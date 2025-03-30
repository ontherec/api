package kr.ontherec.api.modules.chat.dto;

import java.util.Set;

public record ChatCreateRequestDto (
        String title,
        Set<String> participants
) { }
