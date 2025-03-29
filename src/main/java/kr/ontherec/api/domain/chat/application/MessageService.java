package kr.ontherec.api.domain.chat.application;

import kr.ontherec.api.domain.chat.domain.Chat;
import kr.ontherec.api.domain.chat.domain.Message;

import java.util.List;

public interface MessageService {
    List<Message> getPage(Chat chat, Long lastMessageId, int size);
    Message create(Chat chat, Message message);
}
