package kr.ontherec.api.domain.chat.application;

import kr.ontherec.api.domain.chat.domain.Chat;
import kr.ontherec.api.domain.chat.domain.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    List<Message> getPageByChatId(Long chatId, Pageable pageable);
    Message add(Chat chat, Message message);
}