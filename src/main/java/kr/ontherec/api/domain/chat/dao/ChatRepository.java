package kr.ontherec.api.domain.chat.dao;

import kr.ontherec.api.domain.chat.domain.Chat;
import kr.ontherec.api.domain.chat.exception.ChatException;
import kr.ontherec.api.domain.chat.exception.ChatExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryExtension {
    default Chat findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ChatException(ChatExceptionCode.NOT_FOUND));
    }
}