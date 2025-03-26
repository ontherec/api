package kr.ontherec.api.domain.chat.application;

import kr.ontherec.api.domain.chat.domain.Chat;

public interface ChatCommandService {
    Chat create(Chat newChat);
    void read(Chat chat, String username);
    void exit(Chat chat, String username);
}
