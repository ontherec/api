package kr.ontherec.api.modules.chat.dto;

import kr.ontherec.api.modules.chat.entity.MessageType;

public record MessageCreateRequestDto(
        MessageType type,
        String username,
        String content
) { }
