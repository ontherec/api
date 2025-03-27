package kr.ontherec.api.domain.chat.dto;

import kr.ontherec.api.domain.chat.domain.MessageType;

public record MessageCreateRequestDto(
        MessageType type,
        String username,
        String content
) { }
