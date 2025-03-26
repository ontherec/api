package kr.ontherec.api.domain.chat.dto;

import kr.ontherec.api.domain.chat.domain.Participant;

import java.util.Set;

public record ChatResponseDto(
        Long id,
        String title,
        Set<Participant> participants,
        Set<MessageResponseDto> messages
) { }
