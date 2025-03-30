package kr.ontherec.api.modules.chat.application;

import kr.ontherec.api.modules.chat.dao.ChatRepository;
import kr.ontherec.api.modules.chat.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryServiceImpl implements ChatQueryService {
    private final ChatRepository chatRepository;

    @Override
    public List<Chat> getAllByUsername(String username) {
        return chatRepository.findAllByUsername(username);
    }

    @Override
    public Chat get(Long id) {
        return chatRepository.findByIdOrThrow(id);
    }

    @Override
    public boolean isParticipant(Long id, String username) {
        Chat chat = chatRepository.findByIdOrThrow(id);
        return chat.isParticipant(username);
    }
}
