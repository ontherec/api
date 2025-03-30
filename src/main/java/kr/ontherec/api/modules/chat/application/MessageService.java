package kr.ontherec.api.modules.chat.application;

import kr.ontherec.api.modules.chat.entity.Chat;
import kr.ontherec.api.modules.chat.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> getPage(Chat chat, Long lastMessageId, int size);
    Message create(Chat chat, Message message);
}
