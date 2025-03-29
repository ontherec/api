package kr.ontherec.api.domain.chat.dao;

import kr.ontherec.api.domain.chat.domain.Message;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface MessageRepositoryExtension {
    List<Message> findPage(Long chatId, Long lastMessageId, int size);
}