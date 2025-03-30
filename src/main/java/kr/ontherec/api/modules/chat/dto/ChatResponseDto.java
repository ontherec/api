package kr.ontherec.api.modules.chat.dto;

import kr.ontherec.api.modules.chat.entity.Participant;

import java.util.Set;

public record ChatResponseDto(
        Long id,
        String title,
        Set<Participant> participants,
        Set<MessageResponseDto> messages
) { }
