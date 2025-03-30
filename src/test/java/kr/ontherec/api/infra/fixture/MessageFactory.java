package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.modules.chat.application.MessageService;
import kr.ontherec.api.modules.chat.entity.Chat;
import kr.ontherec.api.modules.chat.entity.Message;
import kr.ontherec.api.modules.chat.entity.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageFactory {
    @Autowired private MessageService messageService;

    public Message create(Chat chat, MessageType type, String username, String content) {
        Message newMessage = Message.builder()
                .type(type)
                .username(username)
                .content(content)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        return messageService.create(chat, newMessage);
    }
}
