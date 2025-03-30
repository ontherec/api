package kr.ontherec.api.modules.chat.application;

import kr.ontherec.api.modules.chat.entity.Chat;

public interface ChatCommandService {
    Chat create(Chat newChat);
    void read(Chat chat, String username);
    void exit(Chat chat, String username);
}
