package kr.ontherec.api.modules.chat.application;

import kr.ontherec.api.modules.chat.entity.Chat;

import java.util.List;

public interface ChatQueryService {
    List<Chat> getAllByUsername(String username);
    Chat get(Long id);
    boolean isParticipant(Long id, String username);
}
