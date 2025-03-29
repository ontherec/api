package kr.ontherec.api.domain.chat.dto;

import kr.ontherec.api.domain.chat.domain.MessageType;

import java.time.LocalDateTime;

public record MessageResponseDto(
        Long id,
        MessageType type,
        String username,
        String content,
        LocalDateTime createdAt
) { }
