package kr.ontherec.api.modules.chat.dto;

import kr.ontherec.api.modules.chat.entity.MessageType;

import java.time.LocalDateTime;

public record MessageResponseDto(
        Long id,
        MessageType type,
        String username,
        String content,
        LocalDateTime createdAt
) { }
