package kr.ontherec.api.modules.chat.application;

import kr.ontherec.api.modules.chat.dao.ChatRepository;
import kr.ontherec.api.modules.chat.entity.Chat;
import kr.ontherec.api.modules.chat.entity.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
@Transactional
public class ChatCommandServiceImpl implements ChatCommandService {
    private final ChatRepository chatRepository;

    @Override
    public Chat create(Chat newChat) {
        return chatRepository.save(newChat);
    }

    @Override
    public void read(Chat chat, String username) {
        Participant participant = chat.getParticipant(username);

        participant.updateReadAt();
        chatRepository.save(chat);
    }

    @Override
    public void exit(Chat chat, String username) {
        chat.removeParticipant(username);
        chatRepository.save(chat);
    }
}
