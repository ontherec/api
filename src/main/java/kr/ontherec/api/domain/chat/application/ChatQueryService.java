package kr.ontherec.api.domain.chat.application;

import kr.ontherec.api.domain.chat.domain.Chat;

import java.util.List;

public interface ChatQueryService {
    List<Chat> getAllByUsername(String username);
    Chat get(Long id);
    boolean isParticipant(Long id, String username);
}
