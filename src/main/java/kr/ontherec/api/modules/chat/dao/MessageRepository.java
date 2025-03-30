package kr.ontherec.api.modules.chat.dao;

import kr.ontherec.api.modules.chat.entity.Message;
import kr.ontherec.api.modules.chat.exception.ChatException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static kr.ontherec.api.modules.chat.exception.ChatExceptionCode.NOT_FOUND_MESSAGE;

@Repository
@Transactional
public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryExtension {
    default Message findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ChatException(NOT_FOUND_MESSAGE));
    }
}