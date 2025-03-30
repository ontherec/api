package kr.ontherec.api.modules.chat.application;

import kr.ontherec.api.modules.chat.dao.MessageRepository;
import kr.ontherec.api.modules.chat.entity.Chat;
import kr.ontherec.api.modules.chat.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public List<Message> getPage(Chat chat, Long lastMessageId, int size) {
        return messageRepository.findPage(chat.getId(), lastMessageId, size);
    }

    @Override
    public Message create(Chat chat, Message message) {
        message.setChat(chat);
        return messageRepository.save(message);
    }
}
