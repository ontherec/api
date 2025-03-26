package kr.ontherec.api.domain.chat.application;

import kr.ontherec.api.domain.chat.dao.MessageRepository;
import kr.ontherec.api.domain.chat.domain.Chat;
import kr.ontherec.api.domain.chat.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Service @RequiredArgsConstructor(access = PROTECTED)
@Transactional
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public List<Message> getPageByChatId(Long chatId, Pageable pageable) {
        return messageRepository.findAllByChat_Id(chatId, pageable);
    }

    @Override
    public void send(Chat chat, Message message) {
        message.setChat(chat);
        messageRepository.save(message);
    }
}