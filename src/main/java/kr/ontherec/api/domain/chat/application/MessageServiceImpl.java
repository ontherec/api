package kr.ontherec.api.domain.chat.application;

import kr.ontherec.api.domain.chat.dao.MessageRepository;
import kr.ontherec.api.domain.chat.domain.Chat;
import kr.ontherec.api.domain.chat.domain.Message;
import kr.ontherec.api.domain.chat.exception.ChatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.ontherec.api.domain.chat.exception.ChatExceptionCode.NOT_VALID_MESSAGE;

@Service @RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public List<Message> getPage(Chat chat, Long lastMessageId, int size) {
        if(lastMessageId == null) {
            return messageRepository.findPage(chat.getId(), null, size);
        }

        Message lastMessage = messageRepository.findByIdOrThrow(lastMessageId);
        if(!lastMessage.getChat().equals(chat)) {
            throw new ChatException(NOT_VALID_MESSAGE);
        }

        return messageRepository.findPage(chat.getId(), lastMessage, size);
    }

    @Override
    public Message create(Chat chat, Message message) {
        message.setChat(chat);
        return messageRepository.save(message);
    }
}
